package name.yumao.ffxiv.chn.swing;

import name.yumao.ffxiv.chn.model.Language;
import name.yumao.ffxiv.chn.thread.UnpackThread;
import name.yumao.ffxiv.chn.thread.RepackThread;
import name.yumao.ffxiv.chn.thread.PatchThread;
import name.yumao.ffxiv.chn.util.HexUtils;
import name.yumao.ffxiv.chn.util.res.Config;
import name.yumao.ffxiv.chn.util.FileUtil;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class ProcessPanel extends JFrame implements ActionListener {

    private static final Point origin = new Point();
    private static final String title = "FFXIV Localization Tool";
    private static final String icon = "89504e470d0a1a0a0000000d4948445200000040000000400806000000aa6971de000000097048597300000b1300000b1301009a9c1800000d2449444154789ced9b095415579ec6df74bad3e3a427939eee98d3339938b28382282888209b2c8a86458918dbad359ab84403066d3754542408a844367143f6d51515508c51df7b086adc63dca39ea4a735dd4e27e99c187f73feafdee33db68cca92c4f63be73bdcbaf77fffdf57b7aaeeadaa57a8544ff164203c9c7f0f0bc34cf58f88f070babf16c1cd91a3b8a9fa47c3ebc1384684f3e78830108e0cc15cf5a4203c9c2e9387f22f866d29ff3e8c056322d8317a38f163c2983f3a8c6f4687420383f1503d098889e167e347727eec083e1f1d8aebe8505e1e17cea53121d09493df82b1c395f2d861f4503d09183f8229e383a181217c2b7fa74c84b9a910190b33a2615e16cc5aa4c44c089339807f52fdd4313a90e7df08e3dec461f0f64c98180cbaf22c88db07f11f18b9a40026bdaab44f0a62c40fed5d35219017278770f9cd106e4f1c82eda3f69fecc42f268790337928cc9a06090721be0ae2f72a6553be5703d3c783c44e0e235df543233c9c67a686f2f15b41209c1ecab196e26604f2cb69c12c9e369cd35347706c5a28d95343593d2d8cbca9c1dc91be535f85b852483cd89c09fb60550dc4a4283a5343f86c9c27ffacfaa1317d3063a70f81772240fe4e0fe29b1981bcd8346e660859baf65618350e9665407c01245640d25e88cf87d814589606916361e6089821833c046604f207d50f8d184f7e3e6b18b76606c2f214881a095216be3394cfa6fb6363889d19c45752bf220d566e80d824581a07cb56435c06448f35f6fd5e0ee6feac61bcd7dac417e547d799012c9e1940ff0e1f80a840c6450640f42848d90f8979306f02489d70961fe3743befc90bb23d7b18bc7fa039e74d52e26707f1b77743b8163584af2207f34d5430d7238338a1cb17c8bdc8407a4ff5e457dfb78c4685e8e3fdf9b2c3568777fdc194efad81b4fd0a97cd53eae6047125ca8fe7247eb61fdf49ddc249c63803532b213a00de0de45b437c53ccf6c37ff6431cd1f963c830789aedc749554761ae3fdfcef503e1c2d720a30a32f62b9c1facd4cff5550ccb2929db7f0c80943c639c81e97bf5f1817cfd385ee60cc27e5e10e7e70fe3f61ffdf5b9fc608e1fe3551d850541ec99ef0b8b2320a302d6571bb9e47590b6f901fc6dd1504ecd0fe09e6cc74e6c1c676066a53e7e10f74d35a46e410077170ce2d5eff332df9743bafe265c10c0c1369dfeefb8d225e6fbaeb5409e8f09e4ca221f889f061bab8dcc2c8315e341da0c5c1a0ae9b98de34cb92448898bf1656a8c1f5d17f932aea17f2077749a9ebc60a8136fb2ac4afd426f6a4cb516077076ce20feedb1773ec3895f2cf6e6e2122f3e5aeec34bad0e82272f2ff186a57eb0b9aa39b30a213d0bb28a5b6e37e5ea4520b95a638c27ae4bbdf943a37a5ffebe24808d8bbd7947b617fb722d6610ffa16a2b96bbf362ac17e87931d683ee4d63e2dcf975a22b5d96f9f185217665386cda0ed9558fc14a484b868471b03c005686416a12244736f868e08aa1b02c0062bd9bb7c57ab2b8cd035014ce33cb3db8bf622008970fe456dc007a19da13fc784eeae37cf99f385fee4a3961386c2d87dccac6ccd90599c9b07626ac8a8078b90ff05628e5c408a54d6224b659ffdd4afb4a19147f485d646cdb94adf3a0f368e2b57d66fe78776ec77b405208c8df7877be5ee94e8c5c1249c3795357a767d208c82987fc4a2373ca60dd2c489039c2e3e128b1efbf0dd9858d73b5c6b4687d5f6fbe7ecf9fe2781fb624b8e3d22e0390308073abdc21672ba44de77e823b0f64db94ebe7c3960c28d80d85fb14165440e63c48f436c625f97325d19ff8840178adea8f8d9c41425d79005e895e2c4d1ac4a586dc9e9019ade432e46d89c943f4f11eb8a9da1bc96e6893dd206f337f29d907b999903e01d60c82353eb0210a4af682b41958580aeb5e03e9973c00d6f85195ec86f523685aaff6a22249fabbc1ba914a4e530d53aef656e2d6f4e3f9761f80b5aea8d7f687fc0d7c53b20d8a72a0b402caf6b6ccc26c581708d227c59b3b6bda7054d6f4c72dc593cf2597e494dc2d69a68f50f4d6baf146fbeebd4aa57adf85da75ae9021f7eeae3c90f2fa30d8b6a7394b8b213d002426cd8773a9fde9da56fd94befc26d58b639253728b4653ddbc78455398eac1676bfb1b1fbcda844457baa4f7e3ab7417d0b11f7f4f77e15b296fcb879d7b8cdcb113368e50e2327c385d64c7b3ed624256233b9ecdf0a45e728bc68e5d8db5857973f41e45bfaff2e0d52e03b0de998b99ce9ccbeccb8ccc7ecc5adf8f07592eb0ab1c76571859301bd6f7852c0fee6638f15b553b6383232f660de04fa2911fd9585ba73f47afefcee56c87961fa4da844dae7cb6d11984c5d1b0a7c2c85d85b0c90d36f6d5b5b7ff4cac87e4de201ee400e436f690fbbae26d932b7fd9dc97d0a67db31dda78396e710261c9db50b9bb318bdf54dab67acb8347c7628b3b95a2553ca5b1879d29b07580e2638b2b771bf57166cc6627cadb249ce3c28d9c3e90d30f762542f56e859572d7e702393200ce0fbfd43d2eb6f4c6567ce4ba425599d18730cf03a4ad6018d9a67db6f6e6f0d63ee4b77922caebcbea7c4728f28203bb1456ac00a92bf4e0535527a1c095cba2591167f4515dacf8c877e2aba6f1798e94e6f56249bb8817f502e1c19df081ccc61395ed624f56b78bc0c378e8479c68ee7c43f170b0184a06283e4a5c38d434bec0819ec58e04b78b788933774a1ce0403a7cb813b60f06d92e71c25bd5492875c04734b707291ef64c573c943a71a3c80e8b0e152fef4b69b93d54c7c0919db0cd0564bbcc012b5527619b3dd6a2b9dd55f1b0c357f1506edf093f8cee70a678470fa88901cd0ed8e900b25d64d7fa5ba3f6866889e68e5e7a0fbd150fe5bd78a1a5f89d3df0aaf1e4e76d16aeb0e097bb7b7177b71d1c4983da1d50610fb2bdcd9a7f6db3c0c3fb785e342b1cf41efa281eb65b357e6153a4e299ddb66448db2e3ba63c96d801775cf6d91159f4325df6d893b0d716aabca06ebbc24a6790badd369d7709ecb1c65a34455b3cd4bca678d8ebc87ef12931fb1ce8bac78e4a5dbdd0815b3ccecbd1eade7c5a650355367c5e65cb836a5bd0ae8213db14d6f8e9daa8b4c557d549d867858f68d6f82b1eeab7c0fede7a1f0efcb9ba0fdbab7af257bdef06eeb7a5dba30999d1f5801534d01ab491f0d136238f8e51da0e0d244dd549d86fc732d13c32d6e8e3783a7c30d0c46b13d63870e191e781eaeebcf48125088f04c0f11570babc31eb172bed87fa74de074a87ecb9229af54b9af8298313aba16e2e1ceeaff8127ed8838f8f983de6b3c0e11e5c396c01f56fc1b9f2e63c5b0087ede0b025a87b3cfaf7008f0ab525b6e2e7881d9c2d6cc1cf66507b82c41cb6e4fe611b62cfb4e5d15cdd1dbfa3163c509bc3898970a1182e9435e6f15120ed5a17d4aa0e86c6816ad1aa1fdddcc7f922a8f550bc68ecb87aa45b3bbd18515b334163c17d8d19d4f684e32170211d2e96293c9f091a0bd09883ba175eed22da920f2bdcc583d60a2eac37ea1b783a1a74edd6dcfef0157eddaee247cc71d3da725d04742236f0f11ab85caaf0d4447dbd3df70e5934ff28a2ad909c5a5bee88c6a9378cba065e5c07b57df41ecc9abf0f6837a8bbf392c69a32113a3e18ae962abc5208c7bdf5061c39dfa6ebae092497d69ed392bbde17ae16e9754be05c34d4f555747567a82d271f6bbd7f14d459f15bdd8edac2f51223af6e321e855a473e91c152b5115a1b7e53db9313ba9c7d140d83de9949c61dd758f1bfb53664692c3ae0d57853a0e2673a5173b859d298d7de87638641e8c9176d991334967868edf8427249ce6beb8c3a37721be69d076a33c68b275567e18c1dcf1a26a3dbc5cdf9e92638e1a33f32e650d79bc3ea6e0fbf444a6cad231f1a8eaee4929ca61a37321b8efc9775bf337e4edbe1d09813acb5568e4a9d53cb0320bc950fe76462b434b93e7bf1e93147923566f8c8125563c7af845296ba5a6752b40edc6838ad2de1dc24b85dd0b246fd00fd9c634e64a70d80d646d9793177f6f77065055ccf80db452d9bbc910167229455a361c7fe1fd6da287da46f4b39af2c83532170cc513f003dd9de9903f0498ba66d15539797b43c183773e193057066149cf082630efab3c352294bdd99d7e1d242b895dbf28edfcc818f8636d7d65a75deeb38d5c997784e6343aad69abf6aacf9a2b627276b6df9dcd4507d7fb891defae5d1dabc717d5deb67d2cd6ce3292fd7bdda8a8547cde9a33127446bfe23f842bcd69cffd298f1aed6863fe906c1b9f523d968ae2884b36394c9d2307857e39bc415c1c9410d3760d76b2d7fc4ff1a73d492ffd498f3a5983d3f45bf93b9702d497f849b0c804c7212ab36e73bad15f7743b69a1bf94f4319fe86f713536dc91fcaa1f3bd46624e8ee14dde0c254a8b5335e1e752eca0ec9cc2e4b99ecacc69cef34ddf190e5556d458a6edd77344e8227fcf547dfb29d7ef4ec681c35c3bed944d5934b5a6be55ebee1a16a604379afa1afdcc66a6db8a47fa8e154b012ab1bbc57f89deaa782137ea4d63971bbde853a8db9f29b81bca4d49831aad68e8ba683a3366370a3bedd78416b4b89eecc301dc8579a7fa5f693c5916ed8a8cd98204f99adc6fc37dde436572657b5191132809debf2299ee2299ee229544f34fe0f9d48534cc0c25d1d0000000049454e44ae426082";
    private final JLabel titleLabel = new JLabel(title);

    private final JButton closeButton = new JButton("X");
    private final JPanel titlePanel = new JPanel();
    private final JPanel bodyPanel = new JPanel();

    public JButton unpackButton = new JButton("拆包");
    public JButton repackButton = new JButton("打包");
    public JButton gensenPatchButton = new JButton("源泉字體");
    public JButton pingfangPatchButton = new JButton("蘋方字體");

    private final JLabel langLable = new JLabel("語言");
    private final JComboBox<String> langLableVal = new JComboBox<String>();

    private final JLabel inputPathLable = new JLabel("來源");
    private final JTextField inputPathField = new JTextField(Config.getProperty("InputPath"));
    private final JButton inputPathButton = new JButton("...");


    public ProcessPanel() {
        super(title);
        setUndecorated(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setIconImage(toolkit.createImage(HexUtils.hexStringToBytes(icon)));
        setBounds((toolkit.getScreenSize().width - 500) / 2, (toolkit.getScreenSize().height - 180) / 2, 500, 180);
        setResizable(false);
        setLayout(null);
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
            }
        });

        // 標題面板
        titlePanel.setBounds(0, 0, 500, 30);
        titlePanel.setBackground(new Color(63, 177, 149));
        titlePanel.setBorder(new MatteBorder(0, 0, 0, 0, new Color(63, 177, 149)));
        add(titlePanel);
        titleLabel.setBounds(10, 0, 300, 30);
        titleLabel.setFont(new Font("Microsoft Yahei", Font.BOLD, 14));
        titleLabel.setForeground(new Color(255, 255, 255));
        add(titleLabel, 0);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setBounds(470, 0, 20, 30);
        closeButton.setFont(new Font("Microsoft Yahei", Font.BOLD, 14));
        closeButton.setForeground(new Color(255, 255, 255));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setBorder(null);
        closeButton.setOpaque(false);
        closeButton.setIconTextGap(0);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusable(false);
        closeButton.addActionListener(this);
        add(closeButton,0);

        //主要面板
        bodyPanel.setBounds(0, 30, 500, 150);
        bodyPanel.setBackground(new Color(255, 255, 255));
        bodyPanel.setBorder(new MatteBorder(0, 1, 1, 1, new Color(63, 177, 149)));
        add(bodyPanel);

        // 來源目錄
        inputPathLable.setBounds(20, 45, 50, 30);
        inputPathLable.setFont(new Font("Microsoft Yahei",Font.BOLD,14));
        inputPathLable.setForeground(new Color(10,10,10));
        add(inputPathLable,0);
        inputPathField.setBounds(70, 45, 380, 30);
        inputPathField.setFont(new Font("Microsoft Yahei",Font.BOLD,14));
        inputPathField.setForeground(new Color(110,110,110));
        inputPathField.setEditable(false);
        add(inputPathField,0);
        inputPathButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inputPathButton.setBounds(450, 45, 30, 30);
        inputPathButton.setFont(new Font("Microsoft Yahei",Font.BOLD,14));
        inputPathButton.setForeground(new Color(110,110,110));
        inputPathButton.setMargin(new Insets(0, 0, 0, 0));
        inputPathButton.setOpaque(false);
        inputPathButton.setIconTextGap(0);
        inputPathButton.setContentAreaFilled(false);
        inputPathButton.setFocusable(false);
        inputPathButton.addActionListener(this);
        add(inputPathButton,0);


        langLable.setBounds(20, 85, 50, 30);
        langLable.setFont(new Font("Microsoft Yahei",Font.BOLD,14));
        langLable.setForeground(new Color(10,10,10));
        add(langLable,0);
        langLableVal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        langLableVal.addItem("JA");
        langLableVal.addItem("EN");
        // langLableVal.addItem("DE");
        // langLableVal.addItem("FR");
        langLableVal.addItem("TC");
        langLableVal.addItem("CHS");
        langLableVal.setBounds(70, 85, 180, 30);
        langLableVal.setFont(new Font("Microsoft Yahei",Font.BOLD,14));
        langLableVal.setForeground(new Color(110,110,110));
        langLableVal.setOpaque(false);
        langLableVal.setFocusable(false);
        add(langLableVal,0);

        //拆包
        unpackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        unpackButton.setBounds(20, 130, 70, 34);
        unpackButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,22));
        unpackButton.setForeground(new Color(10,10,10));
        unpackButton.setMargin(new Insets(0, 0, 0, 0));
        unpackButton.setOpaque(false);
        unpackButton.setIconTextGap(0);
        unpackButton.setContentAreaFilled(false);
        unpackButton.setFocusable(false);
        unpackButton.addActionListener(this);
        unpackButton.setEnabled(true);
        add(unpackButton,0);

        //打包
        repackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        repackButton.setBounds(100, 130, 70, 34);
        repackButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,22));
        repackButton.setForeground(new Color(10,10,10));
        repackButton.setMargin(new Insets(0, 0, 0, 0));
        repackButton.setOpaque(false);
        repackButton.setIconTextGap(0);
        repackButton.setContentAreaFilled(false);
        repackButton.setFocusable(false);
        repackButton.addActionListener(this);
        repackButton.setEnabled(true);
        add(repackButton,0);

        // 源泉字體
        gensenPatchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gensenPatchButton.setBounds(230, 130, 120, 34);
        gensenPatchButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,22));
        gensenPatchButton.setForeground(new Color(10,10,10));
        gensenPatchButton.setMargin(new Insets(0, 0, 0, 0));
        gensenPatchButton.setOpaque(false);
        gensenPatchButton.setIconTextGap(0);
        gensenPatchButton.setContentAreaFilled(false);
        gensenPatchButton.setFocusable(false);
        gensenPatchButton.addActionListener(this);
        gensenPatchButton.setEnabled(true);
        add(gensenPatchButton,0);
        
        // 蘋方字體
        pingfangPatchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pingfangPatchButton.setBounds(360, 130, 120, 34);
        pingfangPatchButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,22));
        pingfangPatchButton.setForeground(new Color(10,10,10));
        pingfangPatchButton.setMargin(new Insets(0, 0, 0, 0));
        pingfangPatchButton.setOpaque(false);
        pingfangPatchButton.setIconTextGap(0);
        pingfangPatchButton.setContentAreaFilled(false);
        pingfangPatchButton.setFocusable(false);
        pingfangPatchButton.addActionListener(this);
        pingfangPatchButton.setEnabled(true);
        add(pingfangPatchButton,0);

        setVisible(false);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == inputPathButton) {
            JFileChooser pathChooser = new JFileChooser();
            pathChooser.setDialogTitle("請選擇處理目錄...");
            pathChooser.setCurrentDirectory(new File(Config.getProperty("InputPath")));
            pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = pathChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = pathChooser.getSelectedFile().getPath();
                inputPathField.setText(filePath);
                inputPathField.setCaretPosition(0);
            }
        }
        
        if(e.getSource() == gensenPatchButton) {
            String inputPath = inputPathField.getText();
            if (!hasFontSqpack(inputPath) ) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 000000.win32.dat0 ,000000.win32.index, 000000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                Config.setProperty("InputPath", inputPath);
                Config.saveProperty();
                String[] resourceNames = {"000000.win32.dat0", "000000.win32.index", "000000.win32.index2"};
                for (String resourceName : resourceNames) {
                    File resourceFile = new File(inputPath + File.separator + resourceName);
                    if (resourceFile.exists() && resourceFile.isFile()) {
                        FileUtil.copyToFolder(resourceFile, "output" + File.separator + "output");
                    }
                }
                Config.setProperty("InputPath", inputPath);
                Config.saveProperty();
                PatchThread patchThread = new PatchThread(new File("output" + File.separator + "output").getAbsolutePath(), this);
                Thread patchFileThread = new Thread(patchThread);
                patchFileThread.start();
            }
        }

        if(e.getSource() == pingfangPatchButton) {
            String inputPath = inputPathField.getText();
            if (!hasFontSqpack(inputPath) ) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 000000.win32.dat0 ,000000.win32.index, 000000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                Config.setProperty("InputPath", inputPath);
                Config.saveProperty();
                String[] resourceNames = {"000000.win32.dat0", "000000.win32.index", "000000.win32.index2"};
                for (String resourceName : resourceNames) {
                    File resourceFile = new File(inputPath + File.separator + resourceName);
                    if (resourceFile.exists() && resourceFile.isFile()) {
                        FileUtil.copyToFolder(resourceFile, "output" + File.separator + "output");
                    }
                }
                Config.setProperty("InputPath", inputPath);
                Config.saveProperty();
                PatchThread patchThread = new PatchThread(new File("output" + File.separator + "output").getAbsolutePath(), this);
                Thread patchFileThread = new Thread(patchThread);
                patchFileThread.start();
            }
        }
        
        if(e.getSource() == unpackButton) {
            String inputPath = inputPathField.getText();
            if (!hasDataSqpack(inputPath) ) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 0a0000.win32.dat0 ,0a0000.win32.index, 0a0000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                String lang = Language.toLang((String) langLableVal.getSelectedItem());
                Config.setProperty("InputPath", inputPath);
                Config.setProperty("Language", Language.toLang(lang));
                Config.saveProperty();
                UnpackThread unpackThread = new UnpackThread(inputPath, inputPath, this);
                Thread unpackFileThread = new Thread(unpackThread);
                unpackFileThread.start();
            }
        }

        if(e.getSource() == repackButton) {
            String inputPath = inputPathField.getText();
            if (!hasDataSqpack(inputPath)) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 0a0000.win32.dat0 ,0a0000.win32.index, 0a0000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                String[] resourceNames = {"0a0000.win32.dat0", "0a0000.win32.index", "0a0000.win32.index2"};
                for (String resourceName : resourceNames) {
                    File resourceFile = new File(inputPath + File.separator + resourceName);
                    if (resourceFile.exists() && resourceFile.isFile()) {
                        FileUtil.copyToFolder(resourceFile, "output" + File.separator + "output");
                    }
                }
                String lang = Language.toLang((String) langLableVal.getSelectedItem());
                Config.setProperty("InputPath", inputPath);
                Config.setProperty("Language", Language.toLang(lang));
                Config.saveProperty();
                RepackThread repackThread = new RepackThread(new File("output" + File.separator + "output").getAbsolutePath(), this);
                Thread repackFileThread = new Thread(repackThread);
                repackFileThread.start();
            }
        }

        if (e.getSource() == closeButton) {
            System.exit(0);
        }
    }

    private boolean hasFontSqpack(String path){
        if(path == null)
            return false;
        String[] resourceNames = {"000000.win32.dat0", "000000.win32.index", "000000.win32.index2"};
        for(String resourceName :resourceNames){
            File resourceFile = new File(path + File.separator + resourceName);
            if(!resourceFile.exists() || !resourceFile.isFile()){
                return false;
            }
        }
        return true;
    }
    private boolean hasDataSqpack(String path){
        if(path == null)
            return false;
        String[] resourceNames = {"0a0000.win32.dat0", "0a0000.win32.index", "0a0000.win32.index2"};
        for(String resourceName :resourceNames){
            File resourceFile = new File(path + File.separator + resourceName);
            if(!resourceFile.exists() || !resourceFile.isFile()){
                return false;
            }
        }
        return true;
    }
}
