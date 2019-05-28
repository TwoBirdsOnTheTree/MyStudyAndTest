interface People {
    talk(word: string): string;
}

interface OtherPeople {
    talk(word: string): string;
}

// 鸭式辨行法
var a = {
    talk(word: string) {
        return word;
    }
};



class You implements People {
    talk(word: string): string {
        return word;
    }

}

a.talk("nihao");

//
function fun(p: People) {

}

function funOtherPeople(p: OtherPeople) {

}

fun(a);
funOtherPeople(a);