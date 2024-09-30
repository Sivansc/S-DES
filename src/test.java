public class test {
    public static void createKey(int[] k, int[] k1, int[] k2) {
        int[] temp = new int[11];//用于存储第一次P10的结果
        temp[1] = k[3]; temp[2] = k[5]; temp[3] = k[2]; temp[4] = k[7];
        temp[5] = k[4]; temp[6] = k[10]; temp[7] = k[1]; temp[8] = k[9];
        temp[9] = k[8]; temp[10] = k[6];

        //左右拆分
        int[] l = new int[6], r = new int[6];
        //将数组 temp 中从索引 1 开始的 5 个元素复制到数组 l 中，从索引 1 开始的位置。
        System.arraycopy(temp, 1, l, 1, 5);
        System.arraycopy(temp, 6, r, 1, 5);

        //实现移位操作
        int x1 = l[1], x2 = r[1];
        for (int i = 2; i <= 5; i++) {
            l[i - 1] = l[i];
            r[i - 1] = r[i];
        }
        l[5] = x1; r[5] = x2;

        //分为左右两边移位结果写入temp
        System.arraycopy(l, 1, temp, 1, 5);
        System.arraycopy(r, 1, temp, 6, 5);

        //进行一个P8的压缩置换后即得到k1
        k1[1] = temp[6]; k1[2] = temp[3]; k1[3] = temp[7];
        k1[4] = temp[4]; k1[5] = temp[8]; k1[6] = temp[5];
        k1[7] = temp[10]; k1[8] = temp[9];

        //进行移位，移两次，得到计算K2的中间数据
        x1 = l[1];  x2 = r[1];
        for (int i = 2; i <= 5; i++) {
            l[i - 1] = l[i];
            r[i - 1] = r[i];
        }
        l[5] = x1; r[5] = x2;

        System.arraycopy(l, 1, temp, 1, 5);
        System.arraycopy(r, 1, temp, 6, 5);

        k2[1] = temp[6]; k2[2] = temp[3]; k2[3] = temp[7];
        k2[4] = temp[4]; k2[5] = temp[8]; k2[6] = temp[5];
        k2[7] = temp[10]; k2[8] = temp[9];
    }
    public static void main(String[] args) {
        // 测试用例
        int[] k = {0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1}; // 10位密钥,第一位无效
        int[] k1 = new int[9]; // 存储第一轮密钥
        int[] k2 = new int[9]; // 存储第二轮密钥

        // 预期的结果（可以根据预先计算的结果来设定）
        int[] expectedK1 = {0,1,0,1,0,0,1,0}; // 预期的 K1（根据具体的 P10 和 P8 计算结果设定）
        int[] expectedK2 = {0,1,0,0,0,1,0,1}; // 预期的 K2（根据具体的 P10 和 P8 计算结果设定）

        // 调用 createKey 方法
        createKey(k, k1, k2);

        // 验证结果
        boolean k1Matches = true;
        boolean k2Matches = true;

        for (int i = 1; i <= 8; i++) {
            if (k1[i] != expectedK1[i - 1]) {
                k1Matches = false;
            }
            if (k2[i] != expectedK2[i - 1]) {
                k2Matches = false;
            }
        }

        // 输出测试结果
        if (k1Matches) {
            System.out.println("K1 测试通过: " + intArrayToString(k1));
        } else {
            System.out.println("K1 测试失败: " + intArrayToString(k1) + "，期望: " + intArrayToString1(expectedK1));
        }

        if (k2Matches) {
            System.out.println("K2 测试通过: " + intArrayToString(k2));
        } else {
            System.out.println("K2 测试失败: " + intArrayToString(k2) + "，期望: " + intArrayToString1(expectedK2));
        }
    }
    // 辅助方法，将 int 数组转换为字符串
    public static String intArrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < arr.length; i++) {
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    public static String intArrayToString1(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
        }
        return sb.toString();
    }

}
