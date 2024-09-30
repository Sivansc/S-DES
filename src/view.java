import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// 用户的加密解密界面设计
public class view extends JFrame {
    private JTextField plainTextField;
    private JTextField cipherTextField;
    private JTextField keyField;
    private JButton encryptButton;
    private JButton decryptButton;
    private Controller controller;
    private JButton bruteForceButton;
    private JTextArea resultTextArea; // 添加文本区域
    private JButton asciiEncryptButton; // ASCII 加密按钮
    private JButton asciiDecryptButton; // ASCII 解密按钮


    // 构造函数
    public view() {
        controller = new Controller();
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        // 设置窗口属性
        setLayout(null);
        setTitle("加密解密界面");
        setSize(400, 540);
        setResizable(false);
        setBackground(Color.WHITE);

        // 明文输入框
        plainTextField = new JTextField("请输入明文");
        plainTextField.setBounds(50, 30, 300, 30);
        plainTextField.setForeground(Color.GRAY);
        plainTextField.addFocusListener(new PlaceholderFocusListener(plainTextField, "请输入明文"));
        add(plainTextField);

        // 密文输入框
        cipherTextField = new JTextField("请输入密文");
        cipherTextField.setBounds(50, 70, 300, 30);
        cipherTextField.setForeground(Color.GRAY);
        cipherTextField.addFocusListener(new PlaceholderFocusListener(cipherTextField, "请输入密文"));
        add(cipherTextField);

        // 密钥输入框
        keyField = new JTextField("请输入密钥（10位二进制）");
        keyField.setBounds(50, 110, 300, 30);
        keyField.setForeground(Color.GRAY);
        keyField.addFocusListener(new PlaceholderFocusListener(keyField, "请输入密钥（10位二进制）"));
        add(keyField);

        //初始化文本区
        resultTextArea = new JTextArea();
        resultTextArea.setBounds(50, 240, 300, 200); // 设置位置和大小
        resultTextArea.setEditable(false); // 设置为只读
        add(resultTextArea);

        // 加密按钮
        encryptButton = new JButton("加密");
        encryptButton.setBounds(50, 150, 130, 30);
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    if (key.length() != 10) throw new Exception("密钥必须为10位二进制");
                    int[] k1 = new int[9], k2 = new int[9];
                    int[] k = new int[11];
                    for (int i = 0; i < 10; i++) {
                        k[i + 1] = key.charAt(i) - '0';
                    }
                    controller.createKey(k, k1, k2);

                    String plaintext = plainTextField.getText();
                    //parseint是将十进制字符串转换为int
                    String ciphertext = controller.encode(plaintext, k1, k2); // 获取密文

                    // 将密文显示在密文框内
                    cipherTextField.setText(ciphertext); // 转换为二进制字符串
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        add(encryptButton);

    // 解密按钮
        decryptButton = new JButton("解密");
        decryptButton.setBounds(220, 150, 130, 30);
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    if (key.length() != 10) throw new Exception("密钥必须为10位二进制");
                    int[] k1 = new int[9], k2 = new int[9];
                    int[] k = new int[11];
                    for (int i = 0; i < 10; i++) {
                        k[i + 1] = key.charAt(i) - '0';
                    }
                    controller.createKey(k, k1, k2);

                    String ciphertext = cipherTextField.getText();
                    String plaintexttext = controller.decode(ciphertext, k1, k2); // 获取明文

                    // 将明文显示在明文框内
                    plainTextField.setText(plaintexttext); // 转换为字符串
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        add(decryptButton);

        // 暴力破解按钮
        bruteForceButton = new JButton("暴力破解");
        bruteForceButton.setBounds(50, 200, 300, 30);
        bruteForceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String plaintext = plainTextField.getText();
                    String ciphertext = cipherTextField.getText();

                    // 调用暴力破解函数
                    String foundKeys = controller.bruteForceSDES(plaintext, ciphertext);

                    // 显示找到的密钥
                    if (foundKeys != null) {
                        resultTextArea.setText(foundKeys); // 显示找到的密钥
                    } else {
                        resultTextArea.setText("未找到密钥"); // 显示未找到密钥的消息
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        add(bruteForceButton);

        // ASCII 加密按钮
        asciiEncryptButton = new JButton("ASCII 加密");
        asciiEncryptButton.setBounds(50, 450, 130, 30);
        asciiEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    if (key.length() != 10) throw new Exception("密钥必须为10位二进制");
                    int[] k1 = new int[9], k2 = new int[9];
                    int[] k = new int[11];
                    for (int i = 0; i < 10; i++) {
                        k[i + 1] = key.charAt(i) - '0';
                    }
                    controller.createKey(k, k1, k2);

                    String plaintext = plainTextField.getText();
                    StringBuilder cipherText = new StringBuilder();

                    for (char ch : plaintext.toCharArray()) {
                        // 将字符转换为8位二进制字符串
                        String binaryString = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
                        String cipherValue = controller.encode(binaryString, k1, k2); // 使用修改后的encode方法
                        int cipherAscii = Integer.parseInt(cipherValue, 2); // 转换为 ASCII 码
                        cipherText.append((char) cipherAscii); // 直接添加密文字符
                    }

                    cipherTextField.setText(cipherText.toString()); // 显示加密后的密文
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        add(asciiEncryptButton);


    // ASCII 解密按钮
        asciiDecryptButton = new JButton("ASCII 解密");
        asciiDecryptButton.setBounds(220, 450, 130, 30);
        asciiDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = keyField.getText();
                    if (key.length() != 10) throw new Exception("密钥必须为10位二进制");
                    int[] k1 = new int[9], k2 = new int[9];
                    int[] k = new int[11];
                    for (int i = 0; i < 10; i++) {
                        k[i + 1] = key.charAt(i) - '0';
                    }
                    controller.createKey(k, k1, k2);

                    String ciphertext = cipherTextField.getText();
                    StringBuilder plaintext = new StringBuilder();

                    // 每字符为一个密文字符
                    for (char ch : ciphertext.toCharArray()) {
                        int cipherValue = (int) ch; // 获取字符的 ASCII 值
                        String binaryString = String.format("%8s", Integer.toBinaryString(cipherValue)).replace(' ', '0');
                        String plainValue = controller.decode(binaryString, k1, k2); // 使用修改后的decode方法
                        plaintext.append((char) Integer.parseInt(plainValue, 2)); // 将二进制字符串转换为字符
                    }

                    plainTextField.setText(plaintext.toString()); // 显示解密后的明文
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        add(asciiDecryptButton);


    }

    // Placeholder FocusListener
    private class PlaceholderFocusListener implements FocusListener {
        private final JTextField textField;
        private final String placeholder;

        public PlaceholderFocusListener(JTextField textField, String placeholder) {
            this.textField = textField;
            this.placeholder = placeholder;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setForeground(Color.GRAY);
                textField.setText(placeholder);
            }
        }
    }
}

