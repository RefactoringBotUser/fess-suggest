package jp.sf.fess.suggest.converter;

import com.ibm.icu.text.Transliterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphabetConverter implements SuggestReadingConverter {
    private final Map<String, String[]> convertMap;

    private final KatakanaConverter katakanaConverter;

    protected Transliterator fullWidthHalfWidth;

    protected Transliterator anyLower;

    protected int maxReadingPatternNum = 10;

    public AlphabetConverter() {
        convertMap = generateConvertMapping();
        katakanaConverter = new KatakanaConverter();
        fullWidthHalfWidth = Transliterator.getInstance("Fullwidth-Halfwidth");
        anyLower = Transliterator.getInstance("Any-Lower");
    }

    @Override
    public void start() {
        katakanaConverter.start();
    }

    @Override
    public List<String> convert(String text) {
        List<String> list = new ArrayList<String>();
        List<String> kanaList = katakanaConverter.convert(text);
        if (kanaList.isEmpty()) {
            list.add(text);
            return list;
        }

        for (String kana : kanaList) {
            List<StringBuilder> bufList = new ArrayList<StringBuilder>();
            bufList.add(new StringBuilder());
            for (int i = 0; i < kana.length(); ) {
                String[] alphabets;
                if (i + 1 < kana.length() &&
                        convertMap.get(kana.substring(i, i + 2)) != null) {
                    alphabets = convertMap.get(kana.substring(i, i + 2));
                    i += 2;
                } else if (convertMap.get(kana.substring(i, i + 1)) != null) {
                    alphabets = convertMap.get(kana.substring(i, i + 1));
                    i++;
                } else {
                    alphabets = new String[]{kana.substring(i, i + 1)};
                    i++;
                }

                List<StringBuilder> originBufList = deepCopyBufList(bufList);
                for (int j = 0; j < alphabets.length; j++) {
                    if (j == 0) {
                        for (StringBuilder buf : bufList) {
                            buf.append(alphabets[j]);
                        }
                    } else if (bufList.size() < maxReadingPatternNum) {
                        final List<StringBuilder> tmpBufList = deepCopyBufList(originBufList);
                        for (StringBuilder buf : tmpBufList) {
                            buf.append(alphabets[j]);
                        }
                        bufList.addAll(tmpBufList);
                    }
                }
            }

            for (StringBuilder buf : bufList) {
                String s = fullWidthHalfWidth.transliterate(buf.toString());
                s = anyLower.transliterate(s);
                list.add(s);
            }
        }

        return list;
    }

    private Map<String, String[]> generateConvertMapping() {
        HashMap<String, String[]> map = new HashMap<String, String[]>();

        map.put("ア", new String[]{"a"});
        map.put("イ", new String[]{"i"});
        map.put("ウ", new String[]{"u"});
        map.put("エ", new String[]{"e"});
        map.put("オ", new String[]{"o"});

        map.put("カ", new String[]{"ka"});
        map.put("キ", new String[]{"ki"});
        map.put("ク", new String[]{"ku"});
        map.put("ケ", new String[]{"ke"});
        map.put("コ", new String[]{"ko"});

        map.put("サ", new String[]{"sa"});
        map.put("シ", new String[]{"si", "shi"});
        map.put("ス", new String[]{"su"});
        map.put("セ", new String[]{"se"});
        map.put("ソ", new String[]{"so"});

        map.put("タ", new String[]{"ta"});
        map.put("チ", new String[]{"ti", "chi"});
        map.put("ツ", new String[]{"tu", "tsu"});
        map.put("テ", new String[]{"te"});
        map.put("ト", new String[]{"to"});

        map.put("ナ", new String[]{"na"});
        map.put("ニ", new String[]{"ni"});
        map.put("ヌ", new String[]{"nu"});
        map.put("ネ", new String[]{"ne"});
        map.put("ノ", new String[]{"no"});

        map.put("ハ", new String[]{"ha"});
        map.put("ヒ", new String[]{"hi"});
        map.put("フ", new String[]{"hu", "fu"});
        map.put("ヘ", new String[]{"he"});
        map.put("ホ", new String[]{"ho"});

        map.put("マ", new String[]{"ma"});
        map.put("ミ", new String[]{"mi"});
        map.put("ム", new String[]{"mu"});
        map.put("メ", new String[]{"me"});
        map.put("モ", new String[]{"mo"});

        map.put("ヤ", new String[]{"ya"});
        map.put("ユ", new String[]{"yu"});
        map.put("ヨ", new String[]{"yo"});

        map.put("ラ", new String[]{"ra"});
        map.put("リ", new String[]{"ri"});
        map.put("ル", new String[]{"ru"});
        map.put("レ", new String[]{"re"});
        map.put("ロ", new String[]{"ro"});

        map.put("ワ", new String[]{"wa"});
        map.put("ヲ", new String[]{"wo"});
        map.put("ン", new String[]{"nn", "n"});

        map.put("ガ", new String[]{"ga"});
        map.put("ギ", new String[]{"gi"});
        map.put("グ", new String[]{"gu"});
        map.put("ゲ", new String[]{"ge"});
        map.put("ゴ", new String[]{"go"});

        map.put("ザ", new String[]{"za"});
        map.put("ジ", new String[]{"zi", "ji"});
        map.put("ズ", new String[]{"zu"});
        map.put("ゼ", new String[]{"ze"});
        map.put("ゾ", new String[]{"zo"});

        map.put("ダ", new String[]{"da"});
        map.put("ヂ", new String[]{"di"});
        map.put("ヅ", new String[]{"du"});
        map.put("デ", new String[]{"de"});
        map.put("ド", new String[]{"do"});

        map.put("バ", new String[]{"ba"});
        map.put("ビ", new String[]{"bi"});
        map.put("ブ", new String[]{"bu"});
        map.put("ベ", new String[]{"be"});
        map.put("ボ", new String[]{"bo"});

        map.put("パ", new String[]{"pa"});
        map.put("ピ", new String[]{"pi"});
        map.put("プ", new String[]{"pu"});
        map.put("ペ", new String[]{"pe"});
        map.put("ポ", new String[]{"po"});

        map.put("ヴァ", new String[]{"va"});
        map.put("ヴィ", new String[]{"vi"});
        map.put("ヴ", new String[]{"vu"});
        map.put("ヴェ", new String[]{"ve"});
        map.put("ヴォ", new String[]{"vo"});

        map.put("ギャ", new String[]{"gya"});
        map.put("ギュ", new String[]{"gyu"});
        map.put("ギョ", new String[]{"gyo"});
        map.put("ギェ", new String[]{"gye"});

        map.put("ジャ", new String[]{"zya", "ja", "jya"});
        map.put("ジュ", new String[]{"zyu", "ju", "jyu"});
        map.put("ジョ", new String[]{"zyo", "jo", "jyo"});
        map.put("ジェ", new String[]{"zye", "je", "jye"});

        map.put("キャ", new String[]{"kya"});
        map.put("キュ", new String[]{"kyu"});
        map.put("キョ", new String[]{"kyo"});

        map.put("シャ", new String[]{"sya", "sha"});
        map.put("シュ", new String[]{"syu", "shu"});
        map.put("ショ", new String[]{"syo", "sho"});
        map.put("シェ", new String[]{"sye", "she"});

        map.put("チャ", new String[]{"tya", "cha"});
        map.put("チュ", new String[]{"tyu", "chu"});
        map.put("チョ", new String[]{"tyo", "cho"});
        map.put("チェ", new String[]{"tye", "che"});

        map.put("ニャ", new String[]{"nya"});
        map.put("ニュ", new String[]{"nyu"});
        map.put("ニョ", new String[]{"nyo"});

        map.put("ヒャ", new String[]{"hya"});
        map.put("ヒュ", new String[]{"hyu"});
        map.put("ヒョ", new String[]{"hyo"});

        map.put("フャ", new String[]{"fya"});
        map.put("フュ", new String[]{"hyu", "fyu"});
        map.put("フョ", new String[]{"fyo"});

        map.put("ファ", new String[]{"fa"});
        map.put("フィ", new String[]{"fi"});
        map.put("フェ", new String[]{"fe"});
        map.put("フォ", new String[]{"fo"});

        map.put("ミャ", new String[]{"mya"});
        map.put("ミュ", new String[]{"myu"});
        map.put("ミョ", new String[]{"myo"});

        map.put("リャ", new String[]{"rya"});
        map.put("リュ", new String[]{"ryu"});
        map.put("リョ", new String[]{"ryo"});

        map.put("ァ", new String[]{"a"});
        map.put("ィ", new String[]{"i"});
        map.put("ゥ", new String[]{"u"});
        map.put("ェ", new String[]{"e"});
        map.put("ォ", new String[]{"o"});
        map.put("ャ", new String[]{"ya"});
        map.put("ュ", new String[]{"yu"});
        map.put("ョ", new String[]{"yo"});
        map.put("ッ", new String[]{"tu", "tsu"});

        return map;
    }

    private List<StringBuilder> deepCopyBufList(List<StringBuilder> bufList) {
        List<StringBuilder> list = new ArrayList<StringBuilder>();
        for (StringBuilder buf : bufList) {
            list.add(new StringBuilder(buf.toString()));
        }
        return list;
    }

}
