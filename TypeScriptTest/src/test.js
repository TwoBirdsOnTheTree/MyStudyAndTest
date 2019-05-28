// 鸭式辨行法
var a = {
    talk: function (word) {
        return word;
    }
};
var You = /** @class */ (function () {
    function You() {
    }
    You.prototype.talk = function (word) {
        return word;
    };
    return You;
}());
a.talk("nihao");
//
function fun(p) {
}
function funOtherPeople(p) {
}
fun(a);
funOtherPeople(a);
//# sourceMappingURL=test.js.map