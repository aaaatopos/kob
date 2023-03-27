import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor(info, gamemap) {  // info 是一个哈希表，存放蛇的各种信息{id,color,r,c}
        super();

        this.id = info.id;  // 取出对应蛇的id
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // 用来保存组成该条蛇的所有格子，其中cells[0]存放蛇头
        this.next_cell = null;  // 下一步的目标位置

        this.speed = 5; // 蛇每秒钟走5个格子

        this.direction = -1;  // -1 表示没有指令，0,1,2,3 分别表示上右下左
        this.status = "idle"; // idle 表示静止，move 表示正在移动，die表示死亡。

        // 蛇头移动方向上的偏移量坐标
        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0; //表示回合数，用来判断蛇是否需要变长
        this.eps = 1e-2;  // 允许的误差，当两个点的坐标相差0.01的时候，则认为两个点走在一起了

        this.eye_direction = 0;
        if(this.id === 1) this.eye_direction = 2; // 左下角蛇初始朝上，右上角的蛇朝下

        this.eye_dx = [  // 蛇眼睛不同方向的x的偏移量(两个眼睛)
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [  // 蛇眼睛不同方向的y的偏移量
            [-1, -1],
            [-1, 1,],
            [1, 1],
            [1, -1],
        ];
    }


    start() {
    }

    set_direction(d) {  // 设置下一步的移动方向
        this.direction = d;
    }

    check_tail_increasing() {  // 检查当前回合蛇的长度是否需要增加。
        if(this.step < 10) return true;  // 前10步增加长度
        if(this.step % 3 === 1) return true;  // 大于10步每三步增加一次长度
        return false;
    }

    next_step() {  // 将蛇的状态变为走下一步
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]); // 下一步的格子
        this.eye_direction = d; // 更新蛇眼的方向。
        this.direction = -1;  // 清空方向
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        for(let i = k; i > 0; i --) {  // 蛇的所有节点(格子)向后移动一位
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < this.eps) {  // 当已经走到目标节点了
            this.cells[0] = this.next_cell;  // 更新蛇头位置
            this.next_cell = null;  // 将目标节点清空
            this.status = "idle"; // 走完了，停下来

            if (!this.check_tail_increasing()) {  // 蛇不变长
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta /1000; // 没两帧之间走过的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {  // 不增加长度
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];  // 此时蛇尾的位置，和蛇尾前一个位置。
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                this.x += move_distance * tail_dx / distance;
                this.y += move_distance * tail_dy / distance;
            }
        }
    }


    update() { // 每一帧执行一次，每秒钟执行60次
        if(this.status === "move")
            this.update_move();
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = this.color;
        if(this.status === "die") 
            ctx.fillStyle = "white";
        for(const cell of this.cells) {  // 遍历画蛇的所有身体
            ctx.beginPath(); // 开启画圆的路径
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);  // (圆心横坐标，圆心纵坐标，半径，起始角度，终止角度)
            ctx.fill(); // 填充下颜色
        }

        for(let i = 1; i < this.cells.length; i ++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) 
                continue;
            if(Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        // 绘制眼睛
        ctx.fillStyle = "black";
        for(let i = 0; i < 2; i ++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}