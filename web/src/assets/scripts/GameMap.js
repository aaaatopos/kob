import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        super(); // 执行基类的构造函数，就是AcGameObject的构造函数

        this.ctx = ctx;  // 一个画布
        this.parent = parent;
        this.L = 0;  // 表示地图中每一个小块的宽度

        this.rows = 13;  // 地图的行数
        this.cols = 13;  // 地图的列数

        this.walls = [];  // 保存创建的每个障碍物的对象
        this.inner_walls_count = 40; // 定义在地图里面障碍物数量
    }

    // 判断两个蛇的起点是否连通， 使用Flodd Fill 算法
    check_connectivity(g, sx, sy, tx, ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i ++) {
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) 
                return true;
        }
        return false;
    }

    create_walls() {
        const g = [];  // 布尔数组，如果某个位置是墙，则为true，否则为false
        for(let r = 0; r < this.rows; r ++) {
            g[r] = [];
            for(let c = 0; c < this.cols; c ++) {
                g[r][c] = false;
            }
        }

        // 给四周加上墙
        for(let r = 0; r < this.rows; r ++) 
            g[r][0] = g[r][this.cols - 1] = true;
        for(let c = 0; c < this.cols; c ++) 
            g[0][c] = g[this.rows - 1][c] = true;
        
        // 创建随机障碍物
        for(let i = 0; i < this.inner_walls_count / 2; i ++) {
            for(let j = 0; j < 1000; j ++) {  // 获得随机位置
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r]) continue;  // 该位置已经是障碍物了
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) // 如果是左下角或者是右上角
                    continue; 
                
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g)); // 将g复制一遍
        if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
            return false; // 不连通
        }
        // 绘制墙
        for(let r = 0; r < this.rows; r ++) {
            for(let c = 0; c < this.cols; c ++) {
                if(g[r][c]) 
                    this.walls.push(new Wall(r, c, this));
            }
        }
        return true;
    }

    start() {
        for(let i = 0; i <= 1000; i ++){  // 循环一千次，绘制障碍物，直到连通
            if (this.create_walls())
                break;
        }
    }

    update_size() {  // 更新地图的大小
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
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
