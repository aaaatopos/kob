const AC_GAME_OBJECTS = [];  // 用来保存所有创建的对象

export class AcGameObject {
    constructor() {  // 构造函数
        AC_GAME_OBJECTS.push(this)

        this.has_called_start = false;  // 记录是否执行过start函数
        this.timedelta = 0; // 保存两次执行的时间间隔
    }

    start() {  // 只执行一次
    }

    update() {  // 每一帧执行一次，除了第一帧之外
    
    }

    on_destroy() {  // 删除之前执行

    }

    destroy() {  // 删除对象
        this.on_destroy();

        for(let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if(obj === this){
                AC_GAME_OBJECTS.splice(i);  // 删除该位置的元素
                break;
            }
        }
    }
}

let last_timestamp; // 记录上一次执行的时刻
const step = timestamp => {
    for(let obj of AC_GAME_OBJECTS) {  // of 遍历的是值，in遍历的是下标
        if(!obj.has_called_start){
            obj.start();
            obj.has_called_start = true;
        }
        else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }
    last_timestamp = timestamp; // 更新上一次执行的时刻
    requestAnimationFrame(step);
}

requestAnimationFrame(step);  // 每帧执行一次调用的函数