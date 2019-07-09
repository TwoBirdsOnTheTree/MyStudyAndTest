// 元组
{
    let tuple: [number, string] = [100, 'nihao'];

    //元组可以继续push的, 不限制数量
    tuple.push('third');

    // 但是取的时候提示length = 2
    // let ss = tuple[2];
    let ss = (tuple as any)[2] as string;// ✔

    let test = function (param: [number, string, string]) {

    };
    // 不行, 提示类型不匹配
    // test(tuple);
    // test([...tuple, 'string']); // ✖
    test([tuple[0], tuple[1], 'string']); // ✔
}

// 枚举
// e6没有, 暂时先了解到这吧
{
    // let enum2 = enum Color {}; // ✖
    // 区分Java
    // TS的枚举有点类似数据, 枚举的每一项都是有下标的 (从0开始)
    // 也可以手动指定下标: `red = 100`
    enum Color {white, red = 100, blue = 'string', yellow = 4};

    let c: Color = Color.blue;
    let c1: string = Color['string'];
    let c2: string = Color[0]; // 这个类型反而是string?
    let c3: string = Color[4];

    console.log(`c2: ${c2}, c1: ${c1}, c: ${c}, c3: ${c3}`);
}