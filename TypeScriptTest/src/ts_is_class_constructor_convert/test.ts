class PeopleClass {
    talk(word: string): string {
        return word;
    }
}

interface PeopleInterface {
    talk(word: string): string;

    test?(word: string): void;
}

function test1(p: PeopleClass, p1: PeopleInterface) {
    console.log(p.talk("PeopClass"));
    console.log(p1.talk("PeopleInterfaceClass"));
    console.log(p1.test(""));
}

var a = {
    talk(word: string) {
        return word;
    }
};

// a既看成PeopleClass类型, 也看成了PeopleInterface类型
test1(a, a);

// 匿名接口实例, 比Java多了 class implements
var b = new class implements PeopleInterface {
    talk(word: string): string {
        return "";
    }
}();