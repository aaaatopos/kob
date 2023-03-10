export class Cell{
    constructor(r, c) {
        this.r = r;
        this.c = c;
        // 将横纵坐标替换为canvas的坐标系
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}