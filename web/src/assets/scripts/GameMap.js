import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super(); // 执行基类的构造函数，就是AcGameObject的构造函数

        this.ctx = ctx;  // 一个画布
        this.parent = parent;
        this.store = store;
        this.L = 0;  // 表示地图中每一个小块的宽度

        this.rows = 13;  // 地图的行数
        this.cols = 14;  // 地图的列数

        this.walls = [];  // 保存创建的每个障碍物的对象
        this.inner_walls_count = 20; // 定义在地图里面障碍物数量

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }


    create_walls() {
       const g = this.store.state.pk.gamemap; // 从PK.js中取出后端保存的地图
        // 绘制墙
        for(let r = 0; r < this.rows; r ++) {
            for(let c = 0; c < this.cols; c ++) {
                if(g[r][c]) 
                    this.walls.push(new Wall(r, c, this));
            }
        }
    }

    add_listening_events() {  // 监听 用户输入
        this.ctx.canvas.focus(); // 添加 canvas 聚焦

        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;  // 记录蛇的移动方向
            if(e.key === 'w') d = 0;
            else if(e.key === 'd') d = 1;
            else if(e.key === 's') d = 2;
            else if(e.key === 'a') d = 3;

            if(d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({  // 向后端发送请求
                    event: "move",
                    direction: d,
                }));
            }
        });
    }

    start() {
        this.create_walls();
        this.add_listening_events();
    }

    update_size() {  // 更新地图的大小
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready() {  // 判断两条蛇是否都准备好下一回合
        for(const snake of this.snakes) {
            if(snake.status !== "idle") return false;  // 不是静止，不能进入下一回合
            if(snake.direction === -1) return false;   // 没有给出下一步的指令，则不能进入下一回合
        }
        return true;
    }

    next_step() { // 让两条蛇进入下一回合
        for(const snake of this.snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) {  // 检测目标位置是否合法： 没有撞到两条蛇的身体，或者障碍物
        for(const wall of this.walls) {
            if(wall.r === cell.r && wall.c === cell.c) return false;  // 如果下一位置是障碍物
        }
        for(const snake of this.snakes) {
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候
                k --;
            }
            for(let i = 0; i < k; i ++) {  // 遍历蛇的每个节点，判断是否会装上。
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) 
                    return false;
            }
        }
        return true;
    }

    update() {
        this.update_size();
        if(this.check_ready()) { // 两条蛇都准备好进入下一回合了
            this.next_step();  // 让两条蛇进入下一回合
        }
        this.render();
    }

    render() {
        // 画图 
        const color_even = '#AAD751', color_odd = '#A2D149'  // 定义两种颜色
        for(let r = 0; r < this.rows; r ++) {
            for(let c = 0; c < this.cols; c ++) {
                if((r + c) % 2 == 0) this.ctx.fillStyle = color_even;
                else this.ctx.fillStyle = color_odd;
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }

    destroy() {
    
    }
}
