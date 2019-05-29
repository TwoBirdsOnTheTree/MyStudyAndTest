class PeopleClass {
    talk(word) {
        return word;
    }
}
function test1(p, p1) {
    console.log(p.talk("PeopClass"));
    console.log(p1.talk("PeopleInterfaceClass"));
    console.log(p1.test(""));
}
var a = {
    talk(word) {
        return word;
    }
};
// a既看成PeopleClass类型, 也看成了PeopleInterface类型
test1(a, a);
// 匿名接口实例, 比Java多了 class implements
var b = new class {
    talk(word) {
        return "";
    }
}();
//# sourceMappingURL=test.js.map