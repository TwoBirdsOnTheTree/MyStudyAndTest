package com.thinginginjava.nio;

import com.mytest.io.Util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class UsingBuffers {

  private static void symmetricScramble(CharBuffer buffer){
    Util.printPosLim("开始symmetricScramble", buffer);

    while(buffer.hasRemaining()) {
      buffer.mark();
      char c1 = buffer.get();
      char c2 = buffer.get();
      buffer.reset();
      buffer.put(c2).put(c1);
    }
  }

  public static void main(String[] args) {
    char[] data = "UsingBuffers".toCharArray();
    ByteBuffer bb = ByteBuffer.allocate(data.length * 2);
    CharBuffer cb = bb.asCharBuffer();
    cb.put(data);
    Util.printPosLim("put", cb);

    System.out.println(cb.rewind());
    symmetricScramble(cb);
    System.out.println(cb.rewind());
    symmetricScramble(cb);
    System.out.println(cb.rewind());
  }
} /* Output:
UsingBuffers
sUniBgfuefsr
UsingBuffers
*///:~
