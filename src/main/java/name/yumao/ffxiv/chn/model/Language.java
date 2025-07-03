package name.yumao.ffxiv.chn.model;

public enum Language {
    JA("JA", "JA"),
    EN("EN", "EN");

    private final String name;
    private final String lang;

    Language(String name, String lang){
        this.name = name;
        this.lang = lang;
    }

    public static String toLang(String name){
        for (Language lang: Language.values()) {
            if (lang.name.equals(name)) {
                return lang.lang;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Language.toLang("JA"));
    }

}
