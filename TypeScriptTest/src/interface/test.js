class You {
    talk(word) {
        return word;
    }
}
// 鸭式辨行法
// a没有指定类型
var a = {
    talk(word) {
        return word;
    }
};
a.talk("nihao");
//
function fun(p) {
}
function funOtherPeople(p) {
}
// a可以是People类型
fun(a);
// a也可以是O他和人People类型
funOtherPeople(a);
// 不支持接口的实例化
var you = new You();
//# sourceMappingURL=test.js.map