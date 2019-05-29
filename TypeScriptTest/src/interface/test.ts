interface People {
    talk(word: string): string;
}

interface OtherPeople {
    talk(word: string): string;
}

class You implements People {
    talk(word: string): string {
        return word;
    }

}

// 鸭式辨行法
// a没有指定类型
var a = {
    talk(word: string) {
        return word;
    }
};

a.talk("nihao");

//
function fun(p: People) {

}

function funOtherPeople(p: OtherPeople) {

}

// a可以是People类型
fun(a);
// a也可以是O他和人People类型
funOtherPeople(a);
// 不支持接口的实例化
var you = new You();