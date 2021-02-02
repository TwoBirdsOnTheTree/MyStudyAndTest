import io.netty.buffer.*;
import io.netty.util.ByteProcessor;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NettyByteBufSimpleTest {
    public static void main(String[] args) {

    }

    @Test
    public void first_netty_byte_buf_test() {
        byte[] bytes = "233".getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(bytes)); // 3个

        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);

        int readableBytes = byteBuf.readableBytes();
        System.out.println("readableBytes: " + readableBytes); // 3

        for (int i = 0; i < readableBytes; i++) {
            byte b = byteBuf.readByte();
            System.out.print(new String(new byte[]{b}, StandardCharsets.UTF_8));
        }

        System.out.println("now readableBytes: " + byteBuf.readableBytes()); // 0
        // byteBuf.readByte(); // IndexOutOfBoundsException
    }

    @Test
    public void netty_writer_index_and_reader_index() {
        byte[] bytes = "23".getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);

        System.out.println("reader");

        byteBuf.writeBytes("3".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void test_composite_byte_buf() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("HelloNetty".getBytes(StandardCharsets.UTF_8));

        // CompositeByteBuf compositeByteBuf = new CompositeByteBuf(byteBuf.alloc(), true, 1);

        ByteBuf headerByteBuf = Unpooled.copiedBuffer("http header".getBytes());
        ByteBuf bodyByteBuf = Unpooled.copiedBuffer("http body".getBytes());

        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponents(true, headerByteBuf, bodyByteBuf); // 第一个参数控制是否增加writerIndex, 将影响到CompositeByteBuf是否可读取

        System.out.println("compositeByteBuf readableBytes: " + compositeByteBuf.readableBytes());
        System.out.println("compositeByteBuf readerIndex: " + compositeByteBuf.readerIndex());
        System.out.println("compositeByteBuf writerIndex: " + headerByteBuf.writerIndex());

        System.out.println(new String(new byte[]{compositeByteBuf.readByte()}, StandardCharsets.UTF_8));
        System.out.println("compositeByteBuf readerIndex: " + compositeByteBuf.readerIndex());
    }

    @Test
    public void test_byte_buf_parts() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("233".getBytes(StandardCharsets.UTF_8));

        byteBuf.readByte();

        byte byteIndex0 = byteBuf.getByte(0);
        System.out.println("byteIndex0: " + new String(new byte[]{byteIndex0}, StandardCharsets.UTF_8));

        byteBuf.discardReadBytes();

        byte byteIndex0_1 = byteBuf.getByte(0); // 结果是3, 因为已读的被discard, 然后剩余的被整体移动到顶部
        System.out.println("byteIndex0: " + new String(new byte[]{byteIndex0_1}, StandardCharsets.UTF_8));

        ByteBuf byteBufWrite = Unpooled.buffer(); // 这种ByteBuf的maxCapacity比较大
        System.out.println("byteBufWrite.capacity: " + byteBufWrite.capacity() + ", byteBufWrite.writableBytes: " + byteBufWrite.writableBytes()); // 默认256
        // byteBufWrite.writeBytes("2333".repeat(100).getBytes(StandardCharsets.UTF_8)); // 容量自动扩容到512
        System.out.println("byteBufWrite writerIndex: " + byteBufWrite.writerIndex() + ", readerIndex: " + byteBufWrite.readerIndex()); // writerIndex: 400, readerIndex: 0
    }

    @Test
    public void test_byte_buf_find() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("1234567\n".getBytes(StandardCharsets.UTF_8));

        byte s = 10;
        byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), s);

        int i = byteBuf.forEachByte(ByteProcessor.FIND_CRLF);
        System.out.println(i);
    }

    @Test
    public void test_chinese() {
        try {
            byte[] chinese = "你".getBytes(StandardCharsets.UTF_8);
            System.out.println(chinese.length);
            System.out.println(Arrays.toString(chinese));

            ByteBuf bytes = Unpooled.copiedBuffer(chinese);
            System.out.println(bytes.getChar(0));
            System.out.println(bytes.getCharSequence(0, 3, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_byte_read_write_from_book() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) buf.readByte());
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte) '?');
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }

    @Test
    public void test_byte_buf_holder() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("233".getBytes(StandardCharsets.UTF_8));
        //TODO 作用是? 容器?
        ByteBufHolder byteBufHolder = new DefaultByteBufHolder(byteBuf);
    }
}
