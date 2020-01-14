package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.*;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Objects;

@SpringBootTest
@Disabled
public class TestServiceUnitTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Value("${testservice.chunks_count}")
    private int countChunks;

    @Autowired
    @Qualifier("MockFileWrapper")
    AbstractFileWrapper fileWrapper;

    @Value("${testservice.folder}")
    private String folder;

    @Test
    public void NumberFinderWithByteArrayInputStream_OK() throws Exception {
        assertTest_OK(new NumberFinderWithByteArrayInputStream());
    }

    @Test
    public void NumberFinderWithByteArrayInputStream_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithByteArrayInputStream());
    }

    @Test
    public void NumberFinderWithChunksOfMappedByteBuffer_OK() throws Exception {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksOfMappedByteBuffer_NotFound() throws Exception {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStream_OK() throws Exception {
        NumberFinderWithChunksParallelStream numberFinder = new NumberFinderWithChunksParallelStream();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStream_NotFound() throws Exception {
        NumberFinderWithChunksParallelStream numberFinder = new NumberFinderWithChunksParallelStream();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStreamV2_OK() throws Exception {
        NumberFinderWithChunksParallelStreamV2 numberFinder = new NumberFinderWithChunksParallelStreamV2();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStreamV2_NotFound() throws Exception {
        NumberFinderWithChunksParallelStreamV2 numberFinder = new NumberFinderWithChunksParallelStreamV2();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithMappedByteBuffer_OK() throws Exception {
        assertTest_OK(new NumberFinderWithMappedByteBuffer());
    }

    @Test
    public void NumberFinderWithMappedByteBuffer_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithMappedByteBuffer());
    }

    @Test
    public void NumberFinderWithScanner_OK() throws Exception {
        assertTest_OK(new NumberFinderWithScanner());
    }

    @Test
    public void NumberFinderWithScanner_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithScanner());
    }

    private void assertTest_OK(AbstractNumberFinder numberFinder) throws Exception {
        numberFinder.setFileWrapper(fileWrapper);
        File folder = new File(this.folder);
        File file = Objects.requireNonNull(folder.listFiles())[0];
        boolean actual = numberFinder.findNumberInFile(file, 516854);
        Assert.assertTrue(numberFinder.getClass().getSimpleName() + "_OK - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "_OK - OK");
    }

    private void assertTest_NotFound(AbstractNumberFinder numberFinder) throws Exception {
        numberFinder.setFileWrapper(fileWrapper);
        File folder = new File(this.folder);
        File file = Objects.requireNonNull(folder.listFiles())[0];
        boolean actual = numberFinder.findNumberInFile(file, 123);
        Assert.assertFalse(numberFinder.getClass().getSimpleName() + "_NotFound - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "_NotFound - OK");
    }

    @Test
    @Disabled
    public void tmpTest() throws Exception {
        String mockFile = "68484,46864,84866,48686,33537,786456,456486,484,216546,-6541,516854,2165,-654,9871,1549,6354,548,846383,5482,1568642635,-1655805365,696749136,635141539,-1578671008,930329049,575801810,934946511,-1038330243,418224357,-476374650,-405577837,856589226,-226789608,134324651,39652304,1045709081,1937034725,1641507795,794021858,1010417845,-1724217748,663271584,-1152847674,1286840044,-1865368412,-369107138,-1601795079,-774027262,1164033268,84983964,-288874312,1776547787,-1421086571,-579038128,-1704278869,-997693259,181303972,-2037884685,-1899451294,835283167,-40131211,148579717,-651179919,1532053382,-790721470,-1753344436,1365564578,-1856468085,-1838935674,1485642188,1025555866,-854961596,-1691545953,1511438387,-1547779153,-2045296314,-1795872332,-1153499441,-769299183,1218029135,-2060720928,371809682,1509179175,-715461292,1220823589,-560800257,-1585187343,23283154,-248186703,-1157293420,199128481,1360015924,-1645069393,-1042914043,-1011296505,1380347214,1971803853,-1774037558,1067730774,184739985,2114161765,-1806935608,1229107272,338579000,1485931279,-1340760002,-250711642,1753203471,1971294580,1879879057,-893462942,794298065,1021440413,-1619460210,1101245766,835476016,873322084,1792815177,871166619,-1285471634,-33478434,-161169512,-858132153,-350784762,978717277,571182949,-1677352892,19300014,1450527956,-1378578541,1451944272,552048367,-1531048884,1857505558,-1128615075,-1624410681,1042352775,-1940673977,477989377,1280377333,-185965396,-1851474695,121853864,1875882356,-845086334,422319582,-1552581788,-1159908290,1389663366,800014157,-304386479,-145918772,1021871109,-182400858,2008600109,-1541676599,605382853,1144876747,-1598495278,1466985616,-237095043,219666197,-185321247,-1206901827,498429866,-523265382,-677380071,1704012431,458295,1298903939,-1210540637,-590106926,-181360223,-1255046925,-522016216,-767257292,-1638922183,667345720,-1819468431,1396188464,-1265371171,2067720776,-388712160,-228124381,1573395808,1005503379,-1046531813,870839062,-28838999,-919489688,2109835053,-1426677400,-1806067201,2107573286,1520434739,-81226418,892321802,1183340162,1651503846,1322385298,69731328,1564857713,-1681851873,1071480684,78802064,-422632536,1252720861,330479327,-95247520,-824874680,-1451192993,968831401,-1328034604,-1504779602,343682707,560488836,-1181214965,1844443843,-244082103,1267519320,2006620808,-1578922987,135545173,-669855054,66525363,-1993688389,844008164,-1797122540,112672822,2091911490,1103954160,64613989,-498102074,813008201,-2066332745,963072869,-2105381690,1290018731,2069493561,-801625592,-1330320452,-1987307841,1672577094,-1111423736,38890119,88834055,761379691,401510308,1428195982,-1975544204,-1554605365,-1988951778,-1410155115,-1863633213,1887922446,-550374670,-86460451,-1177414225,2124690488,-242920704,152899471,16711774,769730647,1889676721,-793743772,-1437505374,373004606,1423055110,1153106747,-1828056136,-225991384,714855124,-933132317,-157004351,-1181978627,2036309907,-1579349949,-2017224124,1236297024,-1198458308,1207019374,-805175177,-921495133,673718120,1666089935,2016966257,-1417866866,-311504839,-751821794,-1457971991,2071448160,887854296,1359920320,743181253,-510194742,-23577216,-463924060,-693639740,583846248,1959103982,-1179179854,465624079,-2004721527,-572243730,-1991006460,-1783523305,429778422,-1713676214,-455828029,1109464803,1958884690,1619356056,-855906344,729813905,1812465573,156259992,-1598039412,-1561237002,1526744165,-1781799883,1601961988,1512161464,2116912387,1169854910,-501154151,-482509533,2009708035,-487792586,1004324884,75034642,-42926615,-262855571,571937858,180739385,-797116561,2080935731,1107185992,-1064810822,-1345423908,-122030937,1359029521,-1148992495,362762219,-1048657254,2089520755,18478211,-2089643704,1376316369,-1793359085,-309334398,1417928245,-1111785337,165687349,-1002377841,-387053190,-580000468,-1498511818,894382212,-561964896,-1380841444,364349416,-139596992,1917078278,-1427020491,545977573,-164445531,-810088420,-778671676,-524337343,-313148171,-2130319342,239220731,-648498495,-1915269767,-1495965819,320369671,1071581653,1754275534,1752475232,-993882415,416860100,466885072,-1463295842,-947791780,-985619326,-774532161,345617445,-1644757123,-181170205,-1969646000,-988426979,902633871,-1507787694,-1237175962,-1511770839,-30616784,1382594339,-711152496,1482850683,-1850209111,199607273,-463144278,-2108091033,901985661,-78410583,-975285950,1708766331,-248273898,-1078553658,-1963005579,780908801,-567175735,12301285,979510211,-905892429,475488397,996441221,-1806213551,-1514607117,-849383035,1455582500,1555355708,-1078941628,841742338,-1190167718,1863472794,-520732800,-1005227713,-1219654817,-1888655444,1684391040,-746851185,888395132,-711232988,-2028533358,-601178271,-131954040,-93846812,-432184866,313033798,-124806894,1749400947,412907880,-713562339,372124422,-1930266930,404203426,-1677271365,1885388895,585259433,490858786,-1574471242,-1111257585,1233834064,1883076843,1347975581,1061806190,-1210438947,-895451775,-857086999,400016699,1673143532,387461503,-841724046,-643759161,-533263962,132185205,-1247846181,-1685819514,802903272,12094824,2005944928,874505542,-1188171234,84248672,-332825009,1284570828,1117955284,1291784673,-306761114,1719704818,1731620850,-1300024654,1575812943,-2084857374,-1016810160,-1196916417,1270871900,-79554302,1160510251,649443190,-868552180,-131857453,296048032,-101255350,1141757448,-19999828,-488209476,-961962496,1281930978,1180031172,-112965148,724518545,-2020514198,923577933,-77693180,-253435824,227189626,1709905029,-1552030207,-1366126814,-1354824279,1492858482,-12590579,-1175804500,1628841621,-1895367337,-2139138373,-1790414680,1071087080,551557791,1948203843,930392232,142264775,-12258182,-1801124152,1035525346,-1500354456,-1087097897,-707936496,708898206,-1288826036,-288877851,-241807432,-606271158,-1976391714,512048126,-934327559,1515077143,-1799489957,1865663965,-1262906317,1619170852,385911260,1841946309,-735230035,-1647027957,1901747152,1101716341,1763664513,-1797957007,285045350,-1115384981,333964272,-899285356,2068275209,-160553172,-375473055,-1794026558,-1288178377,462945113,-1334503567,-579709428,-1897332475,1527912313,-2111729065,-1246271432,5000767,1898674414,-2006795190,-844338886,-930592092,814948073,924868405,624831806,-965662628,1324939501,-1832905952,2014623171,-1745299449,-1640838684,-1775884775,-1855347001,-103292986,-164541383,1256305040,1466957089,768274405,-455389665,62267695,2051692497,-1110735287,-99351725,-729206295,254864633,-614206396,1742797649,14890465,-731654853,1585932226,1845484374,1425943034,-1109950459,1238596165,-357232077,-1901201001,430346740,1807130985,-627297472,784281827,881904930,897312455,835477585,-280171560,1399032905,-1905027821,1928136164,-1573615114,1822978149,-43739058,2036119979,297682518,854322921,-1204190396,1917319187,424939436,-582036458,433994112,702361370,1395675997,81196240,-1401102041,1206497105,-2011527164,-976146467,955259513,158621165,1704413303,-718389280,883329318,1456343128,2110321478,-1754001518,1450819751,1091648589,-917654887,915085385,-997016826,1527488954,1120529056,2136356634,1533369856,1224486080,-1053228184,2070203050,-1488333933,1552824657,-787567360,2146384095,-699277453,1482767177,1287965792,452732458,928625004,2029602212,-821373670,844438436,-505127990,-1405098767,-147929240,-498759747,-759444358,-1232937930,-1066812093,37582231,1542221184,-1916481423,1945338259,2103009398,-2124948307,1031726807,652468720,-171236375,373169988,1097022146,46503667,-1967348598,1529006508,1478662083,-2020563534,-457250576,1016058567,1043569413,1416799222,-888948558,1811441784,1679594491,-1418849729,-199860655,1883481370,321423791,-221598180,256967626,485777194,800570261,-82214748,-1070516603,167780930,1898385676,1298187767,1607552352,-569910840,-1468806394,418171189,636576217,-1626991377,-1354556980,1314993047,1963674401,-1521821067,892949721,820088317,-884369864,524641416,-1231935251,-1408974716,1778495039,152456929,-519707273,-1877019699,-759156002,-1066783498,-709095772,1754017101,-2113685830,793181568,-1549489371,-1996424836,-609552778,782684189,-606232825,-1597765136,343380738,1976275621,1168059689,-82822997,1218764832,1352067201,-1521056563,-418902315,1331611061,-613666794,-1063178670,-1426167952,2078382543,588433051,1299561358,582184808,-599160318,181806769,-1082499397,1122036464,437610520,1725114120,-1901810455,-646360592,1106507986,2113990770,-1076765107,-24980774,798659745,1137950666,-2085316030,262781952,-1849816620,1058935854,908166211,1544498319,596088985,617917702,-1524792793,-1252843289,1506005139,723526695,1935819201,91495508,-1207532111,-163471415,-1565810623,526401671,-688568036,1022443772,426765369,758018750,-437733851,1116075826,-2117581206,425628006,2037814513,1292972207,-1450307715,-1187250822,-774168985,-942660528,-757654018,1608278156,-2053391872,-2142167070,-699664327,-358585399,-857612092,2011154946,-1937969620,1426234781,979921092,1933996249,-1537440650,1905799619,-1937157691,354519309,-694898064,513489656,-352066870,1443363214,-2020717871,1508395489,-954521694,1086925032,910002816,472492974,1285939095,1461591937,-64756161,1121573601,-197597533,1957338430,-1120042272,-1409164149,1305268672,-312960505,-418325136,-1854876252,-373097981,504099951,1255677500,-130731936,-953471757,1766045913,-760059847,132431269,452334045,2020452855,570867000,1698986880,-294145548,480629740,-223717597,1322897124,831135392,2066933448,1641005814,-599718841,-573794491,-562452211,1587973750,-1125465922,414507908,1681208302,526389965,-1081354425,1250295515,321763600,377836510,1820689558,1665583919,1498618089,-1230252488,-453781739,451630162,1535051667,-889385134,2076895546,1033196659,135581999,1826745010,1700194903,938346250,-1258447912,-672377314,1242211548,-1087544377,-28516914,1267172983,-1818107486,-294620235,-164611219,1921505271,-24969852,-1063335239,-2098696805,-1026551969,-2082212713,-1457187342,679902886,1492732609,-1059702551,-1345511485,-420391708,-1136692067,553882049,-1052232160,-649710223,977844102,1636858010,545406875,-1936342232,1274584280,-1296615724,-590983686,892588306,-243307974,366142856,-231957789,342666694,-665606452,947770826,199088574,-2012692894,-2053201709,-412908295,-1837778304,-548710726,1941499757,1137731403,1804963968,-1988646782,220457872,-1736568567,-22742474,-132657744,-76144917,-1831536460,1236680523,-971113102,-1913577081,753678676,-1136853302,1060790885,-1157155718,847670106,1935714229,-158085388,1519585372,1113058188,-502505378,805334913,-1428256536,-1724391459,-859661696,1678452708,-2058572025,1111423391,382922897,285249586,-510426565,286083769,-1684658639,1397165346,-414044555,1007467216,-308217461,-489690246,-1048907568,1225199432,-455746300,-1135633658,-818325446,1636210895,1837648945,-1857573490,301532149,1171962978,-610677804,1883477778,1461629168,-1450411734,403129116,622012573,84914184,822032389,1217698345,572342801,-921879424,-767496356,854247993,-672815875,489262947,1409757078,-95250935,-2108982690,460111339,-634037767,1596059444,2098977112,-701281288,-273582289,1419059260,732420406,758873092,-2105543126,1037273568,-387451621,2117616857,1582115539,1615903749,2098831520,-318738415,-2128511523,-1314239502,1165337285,1255281124,801616303,-38900542,1791874040,25819696,1643355771,2131479462,-902814315,1578409463,507073966,1544847747,1611975670,-1347028975,744797070,-1219453183,1825794494,-1642459026,-374226369,-895897202,-2121282872,428404279,-822934884,1467426415,-1575179731,-239963861,-1031481630,1474880171,1754948818,2048126945,-1116252515,1561328141,1636350481,-670546821,-972506229,370253835,-26127697,1804754597,153916528,-1624969382,-1053200540,334167078,-119168948,1876321615,276377821,-1062656303,700008432,266183326,726090768,1522285703,488461223,-1199787444,-599765978,1319088911,1050878403,2000709214,1578860584,612346021,-1129602388,1754407517,1934432401,62330555,-1199098216,-2008650185,281730742,154875862,-1337190927,1138522128,-1324186476,-2098885019,956547147,-1234416751,-1515007593,-369715949,1101787166,-1934189382,221654422,373737452,585609155,492278660,963113919,-215669530,427174596,-412789813,1875158642,-1273727525,-1673697057,-149580389,140433435,135853483,1185209844,-600754092,103246046,-1376424226,1365544041,978860075,237014495,573939602,-1924234139,-756763109,1786411397,-1675598098,83629890,-2055265486,-454149427,998093643,-1883306529,-714909116,1671790189,-25003975,-1324613584,-217150421,257228173,900435449,465770889,-1006425797,-1847343541,-259973366,1132528749,-421273034,808928404,106465516,1316496895,-831479348,-1269861218,853235251,1943063035,412132984,-1197032839,371354500,-1028277810,-751545000,1153505491,-392693575,-1324249732,1127942003,-72976744,369105591,204280152,-1153554643,-1788642319,1223346982,-1075508651,2007695106,-423165721,-841192863,-798812523,56460985,2060911422,-1598065885,1073082029,-1230363569,-709372597,1672359835,354424775,-207781330,1035217180,337815861,1454817308,-1635993854,1771398155,-1928714170,433261186,-1387595561,-726465301,932634141,163773832,250105368,-772888179,1864104010,394594426,1338264196,-1075850304,2052804555,1439790677,1003327269,-265411729,-1147127011,1280316009,-966846167,-614134818,-436635748,-1551463298,874578557,-489584976,-1915089221,1501421350,-1202782443,-452574389,-292043023,1936555978,-1000892486,1663793860,-1969810593,-701029979,1771062055,819805647,-1123574362,25826354,-818369306,-49663791,-683245467,-1376471672,802737786,1553969948,-1047776605,-1765236091,1070849976,-1490183344,-1136451028,1344251359,-760253378,1722486373,860951189,-1499942687,-38362791,1091928403,-925775514,1843932392,2070409876,711735185,-1451408440,-736724485,1644585812,-1057617,2121780958,841449749,-1870336906,-123473276,282311553,2092737595,1979624786,-529462485,1480274749,637143755,-1719948687,1304480090,-861179802,-1054442715,440037730,1396372621,1715135454,442061542,1160567681,-191571488,1911175001,1621428476,838998297,-2118795421,-797460375,1793267223,1379368413,-2093592719,1917130359,-1113393166,153383710,1896967502,-1946715065,82169695,-1656442030,-2052118135,1785181829,-1425341332,-1820746852,1614067489,-1930049255,-1978599469,-832314215,-559532436,301960068,-1135426271,2127360897,2029560994,-1993016430,-2007705333,307753781,-1931701301,-555110167,378298361,2037971176,955585302,1056325442,659227546,-839760419,924485689,-1083742804,-1735484204,-1564145151,-1509100600,-1095310357,-1619833375,1096496789,1378335658,337977006,1127160291,-764088997,1050220641,579885258,-1232115236,413734631,-1245013170,-942862622,-1186572529,311452736,876886864,279031228,-1178079565,794020660,-1903714852,206169934,855723686,-734564393,1954870154,-1965744575,-1962844950,1008673861,-1962735484,809576704,-2122698284,-421018755,1462968868,-1752751519,584603159,1137335159,798264069,387181826,1537057646,416712201,2119209654,1681913313,280138399,-1140636189,1168394959,-460633984,213509532,-1849234555,-1961656538,1637823813,-182632548,74767766,242464976,-738879568,-737335711,928201449,-810395313,-1418155361,-2089338282,-1916733189,2141864283,-330150384,-1213279057,-1451264299,1340226512,-1673756521,-1657342117,-1927493169,-905796362,-1694215956,-1709046801,-860339947,54254";
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        while (byteBuffer.hasRemaining()) {
            System.out.print((char) byteBuffer.get());
        }

        System.out.println();

        File file = Files.createTempFile("tmpTestFile", ".tmp").toFile();
        file.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(mockFile);
        }
        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            ByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 20, file.length()-20);

            System.out.println(mappedByteBuffer.limit());
            System.out.println(byteBuffer.limit());
            byteBuffer.position(byteBuffer.limit()-mappedByteBuffer.limit());
            System.out.println(byteBuffer.position());
            System.out.println(mockFile.substring(byteBuffer.position()));

            while (mappedByteBuffer.hasRemaining()) {
                System.out.print((char) mappedByteBuffer.get());
            }
            NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
        }
    }
}
