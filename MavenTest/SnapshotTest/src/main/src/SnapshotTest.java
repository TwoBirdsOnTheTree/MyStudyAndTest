public class SnapshotTest {
    public static void main(String[] args) {
        // 验证构建时, 是否会重新下载快照版本
        String getFromSnapshotApi = MyApi.getString();

        System.out.println(getFromSnapshotApi);
    }
}
