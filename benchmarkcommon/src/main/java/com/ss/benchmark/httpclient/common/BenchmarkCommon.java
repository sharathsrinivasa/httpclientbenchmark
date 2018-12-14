package com.ss.benchmark.httpclient.common;

/**
 * Created by ssrinivasa on 12/13/18.
 */
public class BenchmarkCommon extends MetricsHelper {

    public static final String ECHO_DELAY_BASE_URL = "/echodelayserv/echo";
    public static final String ECHO_DELAY_POST_SHORT_URL = "/echodelayserv/echo/short";
    public static final String ECHO_DELAY_POST_LONG_URL = "/echodelayserv/echo/long";
    public static final String TEST_ENDPOINT = "/echodelayserv/echo/testmonkey";
    public static final String ECHO_DELAY_SETUP_FULL_URL = "/echodelayserv/delay/uniform?min=1ms&max=2ms";

    //All times are milliseconds unless otherwise noted
    public static final int MAX_CONNECTION_POOL_SIZE = 200;
    public static final int CONNECTION_TTL = 60000;
    public static final int CONNECT_TIMEOUT = 500;
    public static final int CONNECTION_REQUEST_TIMEOUT = 2000;
    public static final int READ_TIMEOUT = 2000;

    public static final int MIN_CONNECTION_POOL_SIZE = 100;
    public static final int MAX_CONNECTION_PER_ROUTE = 200;
    public static final int REQUEST_TIMEOUT = 2000;
    public static final int SOCKET_TIMEOUT = 2000;

    protected static final int EXECUTIONS = 10000;

    protected void setupMetrics() {this.initializeMetrics();}
    protected void tearDownMetrics() {dumpMetrics(); closeMetrics();}
    protected String echoURL(String echophrase){return BenchmarkCommon.ECHO_DELAY_BASE_URL + "/" + echophrase;}
    protected String getBaseUrl() { return "http://localhost:8080";}

    public static final String RANDOM_ECHO_RESPONSE = " {\n" +
            "        \"path\": \"chivas\",\n" +
            "                \"planned-delay\": 464,\n" +
            "                \"real-delay\": 458\n" +
            "        }";

    public static final String SHORT_JSON = "{\"noideanodieaid\":\"100050\",\"meow\":\"8000619521294657390\",\"meowExpiryDate\":\"1812\",\"nodieaLocation\":1,\"protocol\":1,\"projectSpecificData\":{\"@class\":\"somethingasdfklasdjfldksafsdkf dlskjfksdlfjdskalfjsdklfjlsadkf\",\"pursuAssetData\":\"pursu asset data\",\"currencyCode\":\"USD\",\"kfjadklfjdkldjsfkadsjfkadsjfladsjflk\":\"000000050000\"}}";

    public static final String MICRO_JSON = "{\"noideanodieaid\":\"100050\",\"meow\":\"8000619521294657390\",\"meowExpiryDate\":\"1812\",\"nodieaLocation\":1,\"protocol\":1,\"projectSpecificData\":{\"@class\":\"somethingasdfklasdjfldksafsdkf dlskjfksdlfjdskalfjsdklfjlsadkf\",\"pursuAssetData\":\"pursu asset data\",\"currencyCode\":\"USD\",\"kfjadklfjdkldjsfkadsjfkadsjfladsjflk\":\"000000050000\"}}";

    public static final String SMALL_JSON = "[\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"strawberry\",\n" +
            "    \"greeting\": \"Hello, Angelia! You have 7 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Daniel Whitehead\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Sykes Chapman\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Kitty Barton\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"occaecat\",\n" +
            "      \"et\",\n" +
            "      \"in\",\n" +
            "      \"magna\",\n" +
            "      \"cillum\"\n" +
            "    ],\n" +
            "    \"longitude\": \"96.155875\",\n" +
            "    \"latitude\": \"-30.790156\",\n" +
            "    \"registered\": \"Friday, July 17, 2015 7:38 PM\",\n" +
            "    \"about\": \"Enim exercitation voluptate consequat est incididunt mollit culpa id sint non in aliqua. Incididunt fugiat laboris dolore consequat deserunt eu Lorem. Est aute sint tempor ex et ullamco tempor. Aliqua veniam anim Lorem proident aliquip fugiat consequat elit excepteur sunt labore laboris. Mollit dolor fugiat nostrud aliqua ullamco laboris excepteur ad.\",\n" +
            "    \"address\": \"801 Coleridge Street, Axis, Tennessee, 4261\",\n" +
            "    \"phone\": \"+1 (995) 502-2010\",\n" +
            "    \"email\": \"angelia.fuller@exozent.me\",\n" +
            "    \"commeowy\": \"EXOZENT\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Fuller\",\n" +
            "      \"first\": \"Angelia\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"brown\",\n" +
            "    \"age\": 34,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$1,471.59\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"41ea46be-307c-48e2-9f60-1a975d0c2607\",\n" +
            "    \"index\": 0,\n" +
            "    \"_id\": \"57c5e781f4b1faffd191e530\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"apple\",\n" +
            "    \"greeting\": \"Hello, Jacklyn! You have 5 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Chandler Reyes\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Koch Woodward\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Lee Copeland\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"consectetur\",\n" +
            "      \"do\",\n" +
            "      \"proident\",\n" +
            "      \"cupidatat\",\n" +
            "      \"ad\"\n" +
            "    ],\n" +
            "    \"longitude\": \"72.754519\",\n" +
            "    \"latitude\": \"46.615044\",\n" +
            "    \"registered\": \"Monday, January 13, 2014 7:34 PM\",\n" +
            "    \"about\": \"Tempor voluptate laborum consequat non incididunt consequat. Ea commodo ea occaecat anim aliqua commodo cillum aliqua pariatur do veniam. Proident enim consectetur occaecat in enim do veniam id ullamco. Nisi cillum fugiat qui cillum in voluptate ex. Laboris esse et reprehenderit deserunt nisi mollit est reprehenderit. Id sunt nostrud excepteur duis minim voluptate eiusmod id ad magna proident. Nostrud veniam aute non aliquip fugiat.\",\n" +
            "    \"address\": \"811 Oak Street, Enlow, Maine, 1066\",\n" +
            "    \"phone\": \"+1 (922) 592-2639\",\n" +
            "    \"email\": \"jacklyn.dillard@vetron.com\",\n" +
            "    \"commeowy\": \"VETRON\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Dillard\",\n" +
            "      \"first\": \"Jacklyn\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"brown\",\n" +
            "    \"age\": 40,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$3,792.66\",\n" +
            "    \"isActive\": false,\n" +
            "    \"guid\": \"11b598ff-d1f1-4885-ba53-de0334cfcfb0\",\n" +
            "    \"index\": 1,\n" +
            "    \"_id\": \"57c5e7811d2053815d57de79\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"apple\",\n" +
            "    \"greeting\": \"Hello, Graham! You have 6 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Rebekah Quinn\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Gracie Houston\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Herring Kidd\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"esse\",\n" +
            "      \"in\",\n" +
            "      \"cupidatat\",\n" +
            "      \"sit\",\n" +
            "      \"consectetur\"\n" +
            "    ],\n" +
            "    \"longitude\": \"51.560289\",\n" +
            "    \"latitude\": \"32.470092\",\n" +
            "    \"registered\": \"Tuesday, August 4, 2015 7:40 PM\",\n" +
            "    \"about\": \"Minim sunt quis ex ullamco minim mollit dolor consequat culpa eu proident. Duis exercitation id cupidatat consectetur. Eiusmod culpa laboris incididunt ipsum aliquip elit sit magna do. Ex quis velit laboris excepteur aliquip ex duis dolor eu ad elit voluptate. Voluptate sint occaecat et aliqua sunt adipisicing. Magna aute labore adipisicing sint in.\",\n" +
            "    \"address\": \"446 Prospect Place, Deseret, Washington, 6736\",\n" +
            "    \"phone\": \"+1 (940) 474-3391\",\n" +
            "    \"email\": \"graham.kirkland@tropolis.biz\",\n" +
            "    \"commeowy\": \"TROPOLIS\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Kirkland\",\n" +
            "      \"first\": \"Graham\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"brown\",\n" +
            "    \"age\": 21,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$1,229.19\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"46e97611-82a8-40c5-a199-c8c45c01b120\",\n" +
            "    \"index\": 2,\n" +
            "    \"_id\": \"57c5e781f08c2caa8dd9bd28\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"strawberry\",\n" +
            "    \"greeting\": \"Hello, Millie! You have 10 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Earlene Sharpe\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Lorena Young\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Carson Tyler\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"duis\",\n" +
            "      \"sunt\",\n" +
            "      \"excepteur\",\n" +
            "      \"non\",\n" +
            "      \"et\"\n" +
            "    ],\n" +
            "    \"longitude\": \"-168.67032\",\n" +
            "    \"latitude\": \"68.131752\",\n" +
            "    \"registered\": \"Saturday, April 5, 2014 4:12 AM\",\n" +
            "    \"about\": \"Aute elit ad irure nostrud consectetur ullamco laborum cillum elit nulla adipisicing ipsum velit. Dolor et velit excepteur culpa ea fugiat ea et ea. Nostrud ea elit tempor ipsum irure eu do exercitation. Laborum non officia velit quis nostrud eu aliqua deserunt amet incididunt dolore. Cillum commodo ut dolor eu veniam elit esse ullamco voluptate culpa do Lorem. Esse mollit reprehenderit duis labore duis dolore dolore adipisicing. Deserunt nulla reprehenderit exercitation consectetur nostrud dolor culpa sint minim amet.\",\n" +
            "    \"address\": \"403 Classon Avenue, Yonah, Virginia, 5438\",\n" +
            "    \"phone\": \"+1 (890) 497-3606\",\n" +
            "    \"email\": \"millie.cotton@zytrek.us\",\n" +
            "    \"commeowy\": \"ZYTREK\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Cotton\",\n" +
            "      \"first\": \"Millie\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"green\",\n" +
            "    \"age\": 22,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$3,273.57\",\n" +
            "    \"isActive\": false,\n" +
            "    \"guid\": \"0e41454a-010e-4fb8-876b-db0caa782a4b\",\n" +
            "    \"index\": 3,\n" +
            "    \"_id\": \"57c5e781135cdfc8c422904a\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"strawberry\",\n" +
            "    \"greeting\": \"Hello, Kathleen! You have 10 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Adrian Hodge\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Christina Kirk\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Randi Wagner\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"anim\",\n" +
            "      \"reprehenderit\",\n" +
            "      \"proident\",\n" +
            "      \"eu\",\n" +
            "      \"enim\"\n" +
            "    ],\n" +
            "    \"longitude\": \"159.02641\",\n" +
            "    \"latitude\": \"-1.660583\",\n" +
            "    \"registered\": \"Tuesday, March 31, 2015 3:52 AM\",\n" +
            "    \"about\": \"Commodo et non sunt proident. Proident sint qui dolore aliqua labore est commodo ut nisi. Sit anim ea sint occaecat incididunt excepteur. Lorem voluptate aute consequat cupidatat non qui veniam nisi anim nostrud est. Laborum reprehenderit amet elit dolor anim commodo. Dolor Lorem incididunt esse excepteur sint cillum eu ex. Occaecat cillum tempor fugiat veniam incididunt enim cillum irure.\",\n" +
            "    \"address\": \"829 Jay Street, Siglerville, Pennsylvania, 4422\",\n" +
            "    \"phone\": \"+1 (947) 551-3692\",\n" +
            "    \"email\": \"kathleen.mccormick@farmex.info\",\n" +
            "    \"commeowy\": \"FARMEX\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Mccormick\",\n" +
            "      \"first\": \"Kathleen\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"green\",\n" +
            "    \"age\": 32,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$3,448.98\",\n" +
            "    \"isActive\": false,\n" +
            "    \"guid\": \"5d4a2fbe-8ed1-4668-84ea-9e39e13cadd7\",\n" +
            "    \"index\": 4,\n" +
            "    \"_id\": \"57c5e781d269b49b72321cd0\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"banana\",\n" +
            "    \"greeting\": \"Hello, Katina! You have 9 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Mejia Harper\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Cotton Savage\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Steele Chang\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"sit\",\n" +
            "      \"ex\",\n" +
            "      \"enim\",\n" +
            "      \"non\",\n" +
            "      \"exercitation\"\n" +
            "    ],\n" +
            "    \"longitude\": \"-84.638802\",\n" +
            "    \"latitude\": \"8.430178\",\n" +
            "    \"registered\": \"Thursday, July 10, 2014 2:06 AM\",\n" +
            "    \"about\": \"Irure culpa aute commodo dolor aute velit adipisicing reprehenderit proident cillum. Enim non officia consequat do. Esse aliquip et excepteur excepteur anim. Officia qui consectetur veniam dolor excepteur pariatur sint. Est officia veniam qui consectetur ex consequat labore velit eiusmod consequat quis esse. Ullamco occaecat mollit cupidatat laborum elit occaecat.\",\n" +
            "    \"address\": \"380 Royce Place, Bethpage, New York, 4228\",\n" +
            "    \"phone\": \"+1 (933) 510-3019\",\n" +
            "    \"email\": \"katina.tyson@accidency.net\",\n" +
            "    \"commeowy\": \"ACCIDENCY\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Tyson\",\n" +
            "      \"first\": \"Katina\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"blue\",\n" +
            "    \"age\": 24,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$1,447.07\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"8f908dee-cedb-4172-bd1b-5f7474aa71b3\",\n" +
            "    \"index\": 5,\n" +
            "    \"_id\": \"57c5e7816cfeac8bea7458bc\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"apple\",\n" +
            "    \"greeting\": \"Hello, Barry! You have 7 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Clarice Clark\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Talley Lucas\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Burt Haley\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"elit\",\n" +
            "      \"anim\",\n" +
            "      \"consectetur\",\n" +
            "      \"magna\",\n" +
            "      \"elit\"\n" +
            "    ],\n" +
            "    \"longitude\": \"52.198608\",\n" +
            "    \"latitude\": \"-9.150183\",\n" +
            "    \"registered\": \"Monday, October 12, 2015 5:25 AM\",\n" +
            "    \"about\": \"Culpa magna nisi occaecat esse quis labore pariatur consequat fugiat pariatur ullamco dolor tempor. Velit enim aliquip ipsum aute proident eu consectetur excepteur laborum non. Tempor ullamco ipsum nulla incididunt. Anim anim ex fugiat tempor consequat reprehenderit irure adipisicing cupidatat non velit ullamco. Magna labore est dolore veniam incididunt eiusmod.\",\n" +
            "    \"address\": \"947 Sullivan Place, Nogal, Iowa, 5922\",\n" +
            "    \"phone\": \"+1 (880) 561-2295\",\n" +
            "    \"email\": \"barry.kent@tourmania.ca\",\n" +
            "    \"commeowy\": \"TOURMANIA\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Kent\",\n" +
            "      \"first\": \"Barry\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"brown\",\n" +
            "    \"age\": 26,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$2,022.22\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"4afa920f-4fa1-4824-9c02-b7dadce1ef99\",\n" +
            "    \"index\": 6,\n" +
            "    \"_id\": \"57c5e781ade85235b4a7c649\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"apple\",\n" +
            "    \"greeting\": \"Hello, Blanca! You have 6 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Nanette Phillips\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Lizzie Justice\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Mcgowan Wiggins\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"enim\",\n" +
            "      \"cillum\",\n" +
            "      \"aliqua\",\n" +
            "      \"non\",\n" +
            "      \"nostrud\"\n" +
            "    ],\n" +
            "    \"longitude\": \"-126.202325\",\n" +
            "    \"latitude\": \"-83.498247\",\n" +
            "    \"registered\": \"Friday, August 28, 2015 9:36 PM\",\n" +
            "    \"about\": \"Et tempor irure quis culpa deserunt eiusmod fugiat aliqua anim sunt voluptate cupidatat laboris eiusmod. Dolor ut esse anim commodo Lorem cillum ex sunt ut esse. Officia cupidatat sunt Lorem irure ex deserunt dolore dolore ea enim fugiat enim est sit.\",\n" +
            "    \"address\": \"825 Danforth Street, Westphalia, Indiana, 5193\",\n" +
            "    \"phone\": \"+1 (932) 472-2969\",\n" +
            "    \"email\": \"blanca.mcgowan@rooforia.org\",\n" +
            "    \"commeowy\": \"ROOFORIA\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Mcgowan\",\n" +
            "      \"first\": \"Blanca\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"blue\",\n" +
            "    \"age\": 21,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$2,797.24\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"0e8329da-4271-4a06-b16a-42761cf1298f\",\n" +
            "    \"index\": 7,\n" +
            "    \"_id\": \"57c5e781e314c5e314bef2cb\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"banana\",\n" +
            "    \"greeting\": \"Hello, Sheri! You have 5 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Meagan Navarro\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Carol Hardin\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Lindsey Banks\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"veniam\",\n" +
            "      \"Lorem\",\n" +
            "      \"do\",\n" +
            "      \"velit\",\n" +
            "      \"voluptate\"\n" +
            "    ],\n" +
            "    \"longitude\": \"-129.278357\",\n" +
            "    \"latitude\": \"-27.655158\",\n" +
            "    \"registered\": \"Tuesday, February 17, 2015 2:50 PM\",\n" +
            "    \"about\": \"Velit et officia non excepteur officia minim voluptate sit. Voluptate laboris ullamco nulla minim deserunt irure aliqua culpa. Et cupidatat velit consectetur amet proident ea labore do cupidatat anim nulla laboris ea eu. Laborum ipsum cupidatat id ullamco irure cupidatat proident dolor ipsum voluptate dolor. Voluptate quis exercitation est ut pariatur labore.\",\n" +
            "    \"address\": \"717 Osborn Street, Harmon, Nebraska, 1576\",\n" +
            "    \"phone\": \"+1 (869) 475-2256\",\n" +
            "    \"email\": \"sheri.horn@kangle.name\",\n" +
            "    \"commeowy\": \"KANGLE\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Horn\",\n" +
            "      \"first\": \"Sheri\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"brown\",\n" +
            "    \"age\": 21,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$2,434.73\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"e10d5bf5-8b4b-4b94-9589-da55bbc60707\",\n" +
            "    \"index\": 8,\n" +
            "    \"_id\": \"57c5e781571f4ba9c1c6e6f9\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"favoriteFruit\": \"apple\",\n" +
            "    \"greeting\": \"Hello, Luann! You have 9 unread messages.\",\n" +
            "    \"friends\": [\n" +
            "      {\n" +
            "        \"name\": \"Mercer Fitzgerald\",\n" +
            "        \"id\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Lilly Mcmillan\",\n" +
            "        \"id\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Victoria Marquez\",\n" +
            "        \"id\": 2\n" +
            "      }\n" +
            "    ],\n" +
            "    \"range\": [\n" +
            "      0,\n" +
            "      1,\n" +
            "      2,\n" +
            "      3,\n" +
            "      4,\n" +
            "      5,\n" +
            "      6,\n" +
            "      7,\n" +
            "      8,\n" +
            "      9\n" +
            "    ],\n" +
            "    \"tags\": [\n" +
            "      \"ipsum\",\n" +
            "      \"laborum\",\n" +
            "      \"cillum\",\n" +
            "      \"officia\",\n" +
            "      \"excepteur\"\n" +
            "    ],\n" +
            "    \"longitude\": \"7.103635\",\n" +
            "    \"latitude\": \"21.603246\",\n" +
            "    \"registered\": \"Tuesday, September 2, 2014 5:07 PM\",\n" +
            "    \"about\": \"Dolor consequat qui esse occaecat. Qui elit ad culpa laboris dolore. Sint nostrud aute est aute ex. Culpa quis esse laborum commodo exercitation fugiat consequat veniam elit. Pariatur id quis fugiat tempor Lorem fugiat occaecat.\",\n" +
            "    \"address\": \"944 Hull Street, Fidelis, Puerto Rico, 7935\",\n" +
            "    \"phone\": \"+1 (903) 462-2336\",\n" +
            "    \"email\": \"luann.lamb@quotezart.co.uk\",\n" +
            "    \"commeowy\": \"QUOTEZART\",\n" +
            "    \"name\": {\n" +
            "      \"last\": \"Lamb\",\n" +
            "      \"first\": \"Luann\"\n" +
            "    },\n" +
            "    \"eyeColor\": \"green\",\n" +
            "    \"age\": 38,\n" +
            "    \"picture\": \"http://placehold.it/32x32\",\n" +
            "    \"balance\": \"$1,286.95\",\n" +
            "    \"isActive\": true,\n" +
            "    \"guid\": \"888f063b-ada1-422a-a811-3d7fe0e3e022\",\n" +
            "    \"index\": 9,\n" +
            "    \"_id\": \"57c5e7812ab29323c6e47ef6\"\n" +
            "  }\n" +
            "]";

    protected static final String LONG_JSON = "[{\"_id\":\"5769b369dda4cc99eab32929\",\"index\":0,\"guid\":\"d166f7d5-ee5c-46f4-9098-1a36e7052160\",\"isActive\":true,\"balance\":\"$2,081.96\",\"picture\":\"http://placehold.it/32x32\",\"age\":30,\"eyeColor\":\"blue\",\"name\":\"Thomas Pate\",\"gender\":\"male\",\"commeowy\":\"TERRASYS\",\"email\":\"thomaspate@terrasys.com\",\"phone\":\"+1 (931) 414-2375\",\"address\":\"741 Farragut Place, Harold, Arizona, 1855\",\"about\":\"Ullamco aliqua labore sit ea amet officia nostrud qui veniam. Dolor magna irure nostrud tempor esse irure nisi cupidatat id do amet dolore. Esse irure aute sunt est velit adipisicing ad mollit laborum fugiat. Magna veniam reprehenderit sunt do fugiat proident labore non irure minim cupidatat dolore nostrud. Officia dolor pariatur veniam mollit. Mollit minim veniam occaecat fugiat reprehenderit. Aliquip sint anim nulla deserunt mollit nostrud labore Lorem est anim tempor occaecat enim.\\r\\n\",\"registered\":\"2015-02-15T08:04:59 +06:00\",\"latitude\":-29.365153,\"longitude\":94.237735,\"tags\":[\"cillum\",\"eu\",\"quis\",\"ad\",\"aliqua\",\"eiusmod\",\"laboris\"],\"friends\":[{\"id\":0,\"name\":\"Tisha Rowland\"},{\"id\":1,\"name\":\"Flossie Erickson\"},{\"id\":2,\"name\":\"Carroll Ratliff\"}],\"greeting\":\"Hello, Thomas Pate! You have 9 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b369d22e2a2167fbc408\",\"index\":1,\"guid\":\"d836f686-49ce-48c9-80a5-f5849b5e12b8\",\"isActive\":false,\"balance\":\"$3,578.89\",\"picture\":\"http://placehold.it/32x32\",\"age\":27,\"eyeColor\":\"brown\",\"name\":\"Latoya Weaver\",\"gender\":\"female\",\"commeowy\":\"ASSURITY\",\"email\":\"latoyaweaver@assurity.com\",\"phone\":\"+1 (863) 539-3931\",\"address\":\"186 Lyme Avenue, Stollings, Idaho, 2797\",\"about\":\"Tempor officia aliquip do veniam adipisicing et fugiat est occaecat Lorem eiusmod nostrud elit. Qui proident eiusmod mollit mollit reprehenderit laborum sunt nisi laborum. Est consectetur laboris laboris commodo labore ad velit dolore ex elit enim enim id mollit. Irure cillum qui tempor id tempor cupidatat incididunt labore laboris aliqua cupidatat. Non dolore incididunt pariatur mollit dolore consectetur sint exercitation anim ut in labore occaecat. Amet ea enim minim anim. Cupidatat dolor duis quis Lorem duis et aliquip consequat cupidatat non.\\r\\n\",\"registered\":\"2015-07-19T02:11:25 +05:00\",\"latitude\":77.808845,\"longitude\":-167.618263,\"tags\":[\"mollit\",\"dolor\",\"excepteur\",\"eiusmod\",\"culpa\",\"consectetur\",\"elit\"],\"friends\":[{\"id\":0,\"name\":\"Madden Copeland\"},{\"id\":1,\"name\":\"Joseph Knox\"},{\"id\":2,\"name\":\"Shana Lane\"}],\"greeting\":\"Hello, Latoya Weaver! You have 9 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b36908e38e0fd2527d98\",\"index\":2,\"guid\":\"bbdaf4c9-d331-44ac-8e4b-7121f56366ce\",\"isActive\":false,\"balance\":\"$1,686.89\",\"picture\":\"http://placehold.it/32x32\",\"age\":28,\"eyeColor\":\"blue\",\"name\":\"Cheri Beasley\",\"gender\":\"female\",\"commeowy\":\"SNORUS\",\"email\":\"cheribeasley@snorus.com\",\"phone\":\"+1 (989) 454-2793\",\"address\":\"385 Lester Court, Turpin, Wyoming, 9412\",\"about\":\"Aute fugiat Lorem in nostrud quis ipsum laborum cillum magna. Id magna enim exercitation id consectetur ipsum eu do proident nisi elit deserunt culpa tempor. Fugiat ex sint ut veniam ea.\\r\\n\",\"registered\":\"2015-04-05T04:54:41 +05:00\",\"latitude\":71.445086,\"longitude\":17.858452,\"tags\":[\"nisi\",\"ex\",\"adipisicing\",\"est\",\"eu\",\"culpa\",\"ipsum\"],\"friends\":[{\"id\":0,\"name\":\"Hancock Vang\"},{\"id\":1,\"name\":\"Tamra Castillo\"},{\"id\":2,\"name\":\"Rene Spears\"}],\"greeting\":\"Hello, Cheri Beasley! You have 6 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b36968a1523a08db3b57\",\"index\":3,\"guid\":\"9c120849-e42c-423c-b8fb-f39a70ca54b1\",\"isActive\":true,\"balance\":\"$3,688.68\",\"picture\":\"http://placehold.it/32x32\",\"age\":24,\"eyeColor\":\"blue\",\"name\":\"Sheppard Morgan\",\"gender\":\"male\",\"commeowy\":\"APPLICA\",\"email\":\"sheppardmorgan@applica.com\",\"phone\":\"+1 (961) 504-2006\",\"address\":\"833 Dupont Street, Teasdale, Indiana, 5873\",\"about\":\"Non sit et eu et excepteur laboris laboris dolore. Est anim reprehenderit excepteur dolor officia quis duis veniam labore aliqua. Non officia nulla id do irure est anim nisi mollit ex culpa.\\r\\n\",\"registered\":\"2015-01-10T10:37:31 +06:00\",\"latitude\":21.549766,\"longitude\":-148.438034,\"tags\":[\"est\",\"sit\",\"irure\",\"veniam\",\"nisi\",\"aliquip\",\"nulla\"],\"friends\":[{\"id\":0,\"name\":\"Beth West\"},{\"id\":1,\"name\":\"Louella Nicholson\"},{\"id\":2,\"name\":\"Geneva Jacobs\"}],\"greeting\":\"Hello, Sheppard Morgan! You have 4 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b369ec6c5dc1912e95f0\",\"index\":4,\"guid\":\"9bd86cfe-3602-4adc-b6a5-c4adfdd5200f\",\"isActive\":true,\"balance\":\"$3,942.44\",\"picture\":\"http://placehold.it/32x32\",\"age\":31,\"eyeColor\":\"green\",\"name\":\"Myers Gentry\",\"gender\":\"male\",\"commeowy\":\"UNIA\",\"email\":\"myersgentry@unia.com\",\"phone\":\"+1 (971) 541-2650\",\"address\":\"540 Vanderbilt Street, Geyserville, Michigan, 3614\",\"about\":\"Lorem aute incididunt elit qui nulla ut excepteur commodo. Elit quis exercitation ut nulla et culpa id consequat nostrud commodo est non. Minim deserunt magna pariatur nostrud proident do aute. Quis aute esse enim eu reprehenderit esse non ad. Nostrud esse magna ipsum sit do in dolor nisi exercitation elit nulla aliqua anim qui. Amet culpa fugiat deserunt dolor eiusmod non sunt ipsum aliqua id. Fugiat enim aliquip commodo veniam labore dolore minim veniam elit.\\r\\n\",\"registered\":\"2016-04-22T09:17:52 +05:00\",\"latitude\":71.692139,\"longitude\":8.179478,\"tags\":[\"ipsum\",\"quis\",\"id\",\"laboris\",\"consectetur\",\"exercitation\",\"nostrud\"],\"friends\":[{\"id\":0,\"name\":\"Avila Horton\"},{\"id\":1,\"name\":\"Stacey Mays\"},{\"id\":2,\"name\":\"Jodi Mckay\"}],\"greeting\":\"Hello, Myers Gentry! You have 4 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b3696a13ca1be275df4e\",\"index\":5,\"guid\":\"769a3ac9-bc1f-4740-9c47-b7267cc84d32\",\"isActive\":true,\"balance\":\"$1,896.73\",\"picture\":\"http://placehold.it/32x32\",\"age\":40,\"eyeColor\":\"green\",\"name\":\"Clemons Frazier\",\"gender\":\"male\",\"commeowy\":\"XYMONK\",\"email\":\"clemonsfrazier@xymonk.com\",\"phone\":\"+1 (893) 440-2673\",\"address\":\"862 Waldane Court, Ribera, American Samoa, 6476\",\"about\":\"Occaecat enim eiusmod ut nulla reprehenderit. Adipisicing consequat proident est non. Culpa eu velit proident occaecat aliqua. Cupidatat et nulla proident tempor.\\r\\n\",\"registered\":\"2016-03-03T12:37:10 +06:00\",\"latitude\":-78.196417,\"longitude\":-112.077971,\"tags\":[\"aliquip\",\"id\",\"esse\",\"sunt\",\"ullamco\",\"officia\",\"aute\"],\"friends\":[{\"id\":0,\"name\":\"Nina Drake\"},{\"id\":1,\"name\":\"England Melendez\"},{\"id\":2,\"name\":\"Cote Santiago\"}],\"greeting\":\"Hello, Clemons Frazier! You have 6 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b369b41e954577a3401a\",\"index\":6,\"guid\":\"7e0b3584-2908-439c-969e-86826ed4161e\",\"isActive\":true,\"balance\":\"$2,001.36\",\"picture\":\"http://placehold.it/32x32\",\"age\":34,\"eyeColor\":\"green\",\"name\":\"Lesa Conrad\",\"gender\":\"female\",\"commeowy\":\"NORALI\",\"email\":\"lesaconrad@norali.com\",\"phone\":\"+1 (901) 592-3204\",\"address\":\"910 Everit Street, Escondida, Florida, 8902\",\"about\":\"Aliqua ullamco ipsum proident pariatur eiusmod incididunt non commodo ea exercitation duis qui. Fugiat ad culpa velit ipsum. Ea eu dolore non nulla cillum laboris sunt pariatur irure commodo sint non. Qui sint laborum Lorem adipisicing cupidatat culpa enim exercitation. Eu magna dolore veniam et irure exercitation commodo pariatur. Et voluptate officia quis reprehenderit nostrud exercitation ad consequat officia velit.\\r\\n\",\"registered\":\"2015-07-14T10:37:49 +05:00\",\"latitude\":-75.272521,\"longitude\":-82.350124,\"tags\":[\"incididunt\",\"exercitation\",\"culpa\",\"do\",\"reprehenderit\",\"non\",\"ex\"],\"friends\":[{\"id\":0,\"name\":\"Vargas Rojas\"},{\"id\":1,\"name\":\"Baker Lawrence\"},{\"id\":2,\"name\":\"Carver Juarez\"}],\"greeting\":\"Hello, Lesa Conrad! You have 8 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b369fddcc25ceb689196\",\"index\":7,\"guid\":\"496012ea-213f-4b24-94cb-dcc438926018\",\"isActive\":true,\"balance\":\"$3,147.10\",\"picture\":\"http://placehold.it/32x32\",\"age\":28,\"eyeColor\":\"green\",\"name\":\"Gayle Parsons\",\"gender\":\"female\",\"commeowy\":\"EXTRAGENE\",\"email\":\"gayleparsons@extragene.com\",\"phone\":\"+1 (873) 508-3730\",\"address\":\"760 Manor Court, Sidman, Pennsylvania, 3750\",\"about\":\"Culpa magna pariatur laboris commodo nulla sint in dolore quis aute laboris deserunt proident irure. Voluptate nostrud nisi amet qui occaecat amet ex. Nostrud officia tempor Lorem voluptate sunt ullamco. Pariatur ipsum cupidatat aliqua eiusmod fugiat commodo.\\r\\n\",\"registered\":\"2015-05-19T02:30:45 +05:00\",\"latitude\":-77.318525,\"longitude\":-25.821591,\"tags\":[\"ipsum\",\"fugiat\",\"amet\",\"incididunt\",\"nostrud\",\"consequat\",\"dolor\"],\"friends\":[{\"id\":0,\"name\":\"Scott Donovan\"},{\"id\":1,\"name\":\"Tiffany Cunningham\"},{\"id\":2,\"name\":\"Simon Stevens\"}],\"greeting\":\"Hello, Gayle Parsons! You have 4 unread messages.\",\"favoriteFruit\":\"apple\"},{\"_id\":\"5769b3692b089e2ca9877505\",\"index\":8,\"guid\":\"752376db-2b98-46fb-a213-18718ddc14fb\",\"isActive\":true,\"balance\":\"$3,220.29\",\"picture\":\"http://placehold.it/32x32\",\"age\":30,\"eyeColor\":\"blue\",\"name\":\"Odonnell Reyes\",\"gender\":\"male\",\"commeowy\":\"ORBIN\",\"email\":\"odonnellreyes@orbin.com\",\"phone\":\"+1 (872) 419-3600\",\"address\":\"864 Cox Place, Watchtower, Northern Mariana Islands, 6336\",\"about\":\"Pariatur dolor elit ullamco excepteur ut incididunt voluptate. Labore ad id aliquip qui duis. Pariatur adipisicing id pariatur laborum sint elit consectetur ut. Sit voluptate ad et ea enim consequat officia pariatur laboris aliquip.\\r\\n\",\"registered\":\"2015-03-05T09:40:32 +06:00\",\"latitude\":-69.464187,\"longitude\":-160.831665,\"tags\":[\"sunt\",\"aliquip\",\"cillum\",\"nisi\",\"fugiat\",\"aute\",\"aliquip\"],\"friends\":[{\"id\":0,\"name\":\"Summer Lambert\"},{\"id\":1,\"name\":\"Gwen Golden\"},{\"id\":2,\"name\":\"Lolita Lindsay\"}],\"greeting\":\"Hello, Odonnell Reyes! You have 6 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b369081fc89aca38995b\",\"index\":9,\"guid\":\"be6a9fe0-1e13-4aea-9004-d0e64d45ef20\",\"isActive\":false,\"balance\":\"$1,216.36\",\"picture\":\"http://placehold.it/32x32\",\"age\":34,\"eyeColor\":\"blue\",\"name\":\"Mcdonald Harris\",\"gender\":\"male\",\"commeowy\":\"NAVIR\",\"email\":\"mcdonaldharris@navir.com\",\"phone\":\"+1 (807) 404-2299\",\"address\":\"203 Seigel Court, Newkirk, Hawaii, 8291\",\"about\":\"Eiusmod aliquip officia qui fugiat irure aute id id duis occaecat eu. Occaecat ullamco occaecat amet amet qui nisi duis nulla consequat laborum anim eu labore dolor. Nisi cupidatat ex veniam nulla adipisicing ad quis mollit cillum amet amet ea aliquip. Est duis consequat veniam et commodo fugiat dolor duis laboris id amet veniam.\\r\\n\",\"registered\":\"2014-06-01T05:26:00 +05:00\",\"latitude\":-65.128068,\"longitude\":-151.030121,\"tags\":[\"nulla\",\"consequat\",\"labore\",\"irure\",\"incididunt\",\"culpa\",\"consectetur\"],\"friends\":[{\"id\":0,\"name\":\"Mable Ortega\"},{\"id\":1,\"name\":\"Dixie Palmer\"},{\"id\":2,\"name\":\"Shaw Grimes\"}],\"greeting\":\"Hello, Mcdonald Harris! You have 2 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b369b1177ab375a4ebe6\",\"index\":10,\"guid\":\"07de09be-9a6e-4b92-9efb-f1faca8f9226\",\"isActive\":true,\"balance\":\"$1,479.40\",\"picture\":\"http://placehold.it/32x32\",\"age\":36,\"eyeColor\":\"green\",\"name\":\"Hood Bryant\",\"gender\":\"male\",\"commeowy\":\"EXPOSA\",\"email\":\"hoodbryant@exposa.com\",\"phone\":\"+1 (886) 554-2747\",\"address\":\"932 Cass Place, Edmund, Maryland, 7059\",\"about\":\"Mollit occaecat aute duis culpa est. Ullamco dolore tempor sint dolore adipisicing sit cupidatat nisi aute ex. Occaecat commodo commodo Lorem elit irure dolor ullamco. Dolor id do amet adipisicing do ex commodo pariatur consequat proident.\\r\\n\",\"registered\":\"2015-10-17T04:21:03 +05:00\",\"latitude\":84.430361,\"longitude\":-57.02466,\"tags\":[\"minim\",\"deserunt\",\"incididunt\",\"irure\",\"culpa\",\"ullamco\",\"anim\"],\"friends\":[{\"id\":0,\"name\":\"Elliott Ramos\"},{\"id\":1,\"name\":\"Hinton Wilkerson\"},{\"id\":2,\"name\":\"Shirley Mathews\"}],\"greeting\":\"Hello, Hood Bryant! You have 5 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b3693b93f8cf0f1418b5\",\"index\":11,\"guid\":\"88850e64-e4f6-4783-b4d8-8684b52d8936\",\"isActive\":false,\"balance\":\"$2,928.03\",\"picture\":\"http://placehold.it/32x32\",\"age\":32,\"eyeColor\":\"blue\",\"name\":\"Evelyn Norman\",\"gender\":\"female\",\"commeowy\":\"VELITY\",\"email\":\"evelynnorman@velity.com\",\"phone\":\"+1 (980) 408-2966\",\"address\":\"720 Juliana Place, Bynum, Minnesota, 3134\",\"about\":\"Labore ea consequat id nisi. Labore eu eu id sit laborum irure officia cillum tempor pariatur id eiusmod officia. Voluptate officia ut ad adipisicing pariatur in proident. Nisi est nostrud occaecat non sint laborum in consectetur.\\r\\n\",\"registered\":\"2015-01-29T10:09:29 +06:00\",\"latitude\":-54.989431,\"longitude\":30.334336,\"tags\":[\"quis\",\"do\",\"nulla\",\"minim\",\"in\",\"laboris\",\"id\"],\"friends\":[{\"id\":0,\"name\":\"Harper Justice\"},{\"id\":1,\"name\":\"Kirsten Kerr\"},{\"id\":2,\"name\":\"Lisa Reid\"}],\"greeting\":\"Hello, Evelyn Norman! You have 1 unread messages.\",\"favoriteFruit\":\"apple\"},{\"_id\":\"5769b3696cf0889fa400f68f\",\"index\":12,\"guid\":\"f9bb4ecc-f25a-4573-9055-bb10306cfb04\",\"isActive\":false,\"balance\":\"$1,117.26\",\"picture\":\"http://placehold.it/32x32\",\"age\":24,\"eyeColor\":\"blue\",\"name\":\"Lorie Jackson\",\"gender\":\"female\",\"commeowy\":\"PLAYCE\",\"email\":\"loriejackson@playce.com\",\"phone\":\"+1 (836) 478-2659\",\"address\":\"125 Oceanview Avenue, Gardners, Kentucky, 3603\",\"about\":\"Esse eiusmod eu commodo nostrud officia adipisicing Lorem. Eiusmod dolore exercitation dolor sit sit nulla excepteur nulla et eiusmod voluptate. Dolor eu dolor tempor incididunt cillum.\\r\\n\",\"registered\":\"2015-08-04T03:55:50 +05:00\",\"latitude\":-51.337019,\"longitude\":31.627782,\"tags\":[\"mollit\",\"officia\",\"Lorem\",\"magna\",\"irure\",\"adipisicing\",\"fugiat\"],\"friends\":[{\"id\":0,\"name\":\"Ruiz Cortez\"},{\"id\":1,\"name\":\"Mcmahon Marquez\"},{\"id\":2,\"name\":\"Cummings Ryan\"}],\"greeting\":\"Hello, Lorie Jackson! You have 1 unread messages.\",\"favoriteFruit\":\"apple\"},{\"_id\":\"5769b369ad4d052694e8652e\",\"index\":13,\"guid\":\"c5feb16d-289d-4927-8c69-f5004288d8c5\",\"isActive\":false,\"balance\":\"$3,983.75\",\"picture\":\"http://placehold.it/32x32\",\"age\":28,\"eyeColor\":\"green\",\"name\":\"Chasity Quinn\",\"gender\":\"female\",\"commeowy\":\"WAAB\",\"email\":\"chasityquinn@waab.com\",\"phone\":\"+1 (967) 448-3454\",\"address\":\"442 Hopkins Street, Guthrie, Mississippi, 2206\",\"about\":\"Ut sint amet mollit laboris esse. Ad adipisicing veniam consequat pariatur reprehenderit qui pariatur pariatur in aute. Labore cillum consectetur tempor laboris consequat consequat aute pariatur Lorem excepteur ad ea eiusmod. Ut duis ex anim magna voluptate. Quis aliquip qui cupidatat velit excepteur incididunt culpa consectetur anim tempor consequat. Et ea minim incididunt in cupidatat do ex labore.\\r\\n\",\"registered\":\"2016-06-16T04:57:38 +05:00\",\"latitude\":-13.676972,\"longitude\":130.678272,\"tags\":[\"aliqua\",\"mollit\",\"veniam\",\"veniam\",\"aliqua\",\"proident\",\"esse\"],\"friends\":[{\"id\":0,\"name\":\"Hull Baird\"},{\"id\":1,\"name\":\"Lilia Hanson\"},{\"id\":2,\"name\":\"Juliana Becker\"}],\"greeting\":\"Hello, Chasity Quinn! You have 2 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b36941041e129ff9ac56\",\"index\":14,\"guid\":\"8a88c9aa-a0c3-4534-9c83-a5394c488271\",\"isActive\":true,\"balance\":\"$1,521.68\",\"picture\":\"http://placehold.it/32x32\",\"age\":24,\"eyeColor\":\"brown\",\"name\":\"Cindy Snow\",\"gender\":\"female\",\"commeowy\":\"ECRATER\",\"email\":\"cindysnow@ecrater.com\",\"phone\":\"+1 (917) 487-3414\",\"address\":\"462 Ovington Court, Sunwest, North Dakota, 2463\",\"about\":\"Cupidatat cupidatat tempor laboris eiusmod deserunt sint nostrud non laboris culpa nisi voluptate. Veniam Lorem aute dolore tempor ipsum Lorem ullamco aliquip amet nisi. Consequat Lorem adipisicing commodo sit eu amet anim consectetur culpa eu aliqua. Enim Lorem duis cillum nostrud cupidatat aute voluptate id magna magna qui. Voluptate cupidatat aute eu quis enim duis eiusmod officia. Lorem laborum cillum consectetur velit velit ex deserunt exercitation.\\r\\n\",\"registered\":\"2016-03-07T05:01:16 +06:00\",\"latitude\":-14.062694,\"longitude\":-105.132809,\"tags\":[\"quis\",\"labore\",\"ad\",\"non\",\"exercitation\",\"esse\",\"adipisicing\"],\"friends\":[{\"id\":0,\"name\":\"Gates Myers\"},{\"id\":1,\"name\":\"Viola Shields\"},{\"id\":2,\"name\":\"Hendricks Howe\"}],\"greeting\":\"Hello, Cindy Snow! You have 2 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b3692e656955d756abe3\",\"index\":15,\"guid\":\"9e55d623-ceba-45f4-8d32-69e684e61ff4\",\"isActive\":true,\"balance\":\"$3,531.80\",\"picture\":\"http://placehold.it/32x32\",\"age\":28,\"eyeColor\":\"green\",\"name\":\"Nelson Bailey\",\"gender\":\"male\",\"commeowy\":\"ASIMILINE\",\"email\":\"nelsonbailey@asimiline.com\",\"phone\":\"+1 (964) 502-3050\",\"address\":\"229 Kermit Place, Sutton, California, 6666\",\"about\":\"Nostrud qui consequat nostrud nisi aute cillum minim. Cillum duis Lorem nostrud anim do minim proident veniam cupidatat. Ut laborum elit nulla mollit ut magna ullamco dolore nulla.\\r\\n\",\"registered\":\"2015-06-08T11:38:35 +05:00\",\"latitude\":15.529523,\"longitude\":138.116014,\"tags\":[\"adipisicing\",\"nisi\",\"adipisicing\",\"fugiat\",\"nulla\",\"esse\",\"velit\"],\"friends\":[{\"id\":0,\"name\":\"Reynolds Murray\"},{\"id\":1,\"name\":\"Marilyn Rutledge\"},{\"id\":2,\"name\":\"Mercedes Stark\"}],\"greeting\":\"Hello, Nelson Bailey! You have 8 unread messages.\",\"favoriteFruit\":\"apple\"},{\"_id\":\"5769b369824f1f9dc3568924\",\"index\":16,\"guid\":\"e5e02ef5-3555-45c0-b5d1-4ee04715f07c\",\"isActive\":true,\"balance\":\"$2,706.02\",\"picture\":\"http://placehold.it/32x32\",\"age\":20,\"eyeColor\":\"blue\",\"name\":\"Chapman Villarreal\",\"gender\":\"male\",\"commeowy\":\"ROCKLOGIC\",\"email\":\"chapmanvillarreal@rocklogic.com\",\"phone\":\"+1 (902) 575-2914\",\"address\":\"639 Lombardy Street, Kiskimere, New Mexico, 1123\",\"about\":\"Commodo ut quis dolor laborum velit consectetur occaecat culpa. Ut esse amet voluptate ad sint fugiat mollit excepteur est dolore nulla. Duis non do cillum Lorem ad exercitation excepteur dolor aliqua quis veniam consectetur. Incididunt ut occaecat aute est fugiat amet enim ipsum aliquip et Lorem ullamco labore. Ad non duis ullamco commodo veniam exercitation esse excepteur Lorem occaecat elit id officia.\\r\\n\",\"registered\":\"2016-04-28T10:29:51 +05:00\",\"latitude\":18.489463,\"longitude\":146.965625,\"tags\":[\"ut\",\"pariatur\",\"id\",\"est\",\"eiusmod\",\"qui\",\"consectetur\"],\"friends\":[{\"id\":0,\"name\":\"Louise Wong\"},{\"id\":1,\"name\":\"Briggs Davidson\"},{\"id\":2,\"name\":\"Benita Reilly\"}],\"greeting\":\"Hello, Chapman Villarreal! You have 1 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b369955ee280578df375\",\"index\":17,\"guid\":\"db51619c-eae5-4b96-9b9a-16cb8e37617b\",\"isActive\":true,\"balance\":\"$1,686.16\",\"picture\":\"http://placehold.it/32x32\",\"age\":36,\"eyeColor\":\"blue\",\"name\":\"Mullen Bonner\",\"gender\":\"male\",\"commeowy\":\"ERSUM\",\"email\":\"mullenbonner@ersum.com\",\"phone\":\"+1 (855) 553-3138\",\"address\":\"213 Plaza Street, Dixie, Massachusetts, 6115\",\"about\":\"Ut ex irure cillum aliqua duis eiusmod esse. Adipisicing et elit in sint in. Nulla sint ut aliquip aliquip cupidatat adipisicing. Sit nisi amet laboris qui et anim occaecat cillum officia qui deserunt occaecat velit culpa.\\r\\n\",\"registered\":\"2014-10-18T10:05:20 +05:00\",\"latitude\":-86.494459,\"longitude\":145.989238,\"tags\":[\"ut\",\"laboris\",\"esse\",\"est\",\"est\",\"laborum\",\"occaecat\"],\"friends\":[{\"id\":0,\"name\":\"Glenda Forbes\"},{\"id\":1,\"name\":\"Ebony Fletcher\"},{\"id\":2,\"name\":\"Mcgee Obrien\"}],\"greeting\":\"Hello, Mullen Bonner! You have 6 unread messages.\",\"favoriteFruit\":\"strawberry\"},{\"_id\":\"5769b369508f11e8616e4736\",\"index\":18,\"guid\":\"e9167631-76a6-409f-883b-66672ff56697\",\"isActive\":false,\"balance\":\"$3,938.96\",\"picture\":\"http://placehold.it/32x32\",\"age\":32,\"eyeColor\":\"brown\",\"name\":\"Queen Mccray\",\"gender\":\"female\",\"commeowy\":\"FLYBOYZ\",\"email\":\"queenmccray@flyboyz.com\",\"phone\":\"+1 (865) 455-2669\",\"address\":\"502 Bushwick Place, Rutherford, Kansas, 1141\",\"about\":\"Sint commodo ea ad duis laboris ex officia in do sit ipsum exercitation. Ad irure nulla id non sint in magna mollit Lorem exercitation. Aliquip fugiat nisi id et enim non adipisicing est anim eu. Mollit sint minim Lorem duis in quis eu ipsum officia. Sint anim duis tempor exercitation reprehenderit reprehenderit qui fugiat qui ut dolore do. Aliquip officia quis velit fugiat aliquip sint cillum.\\r\\n\",\"registered\":\"2014-09-16T09:34:36 +05:00\",\"latitude\":-86.530476,\"longitude\":175.43942,\"tags\":[\"nulla\",\"qui\",\"incididunt\",\"magna\",\"ullamco\",\"nulla\",\"elit\"],\"friends\":[{\"id\":0,\"name\":\"Enid Beach\"},{\"id\":1,\"name\":\"Naomi Walters\"},{\"id\":2,\"name\":\"Duke Durham\"}],\"greeting\":\"Hello, Queen Mccray! You have 8 unread messages.\",\"favoriteFruit\":\"apple\"},{\"_id\":\"5769b3695d235198963d8127\",\"index\":19,\"guid\":\"5388697f-92da-4ded-9c87-582de86d816b\",\"isActive\":false,\"balance\":\"$1,997.63\",\"picture\":\"http://placehold.it/32x32\",\"age\":23,\"eyeColor\":\"blue\",\"name\":\"Vega Atkins\",\"gender\":\"male\",\"commeowy\":\"PROSELY\",\"email\":\"vegaatkins@prosely.com\",\"phone\":\"+1 (849) 483-2259\",\"address\":\"325 Franklin Avenue, Chesterfield, Tennessee, 1990\",\"about\":\"Nostrud occaecat do esse reprehenderit voluptate. Nisi nostrud eiusmod velit excepteur aliquip sit. Laboris excepteur in nostrud ex. Do veniam commodo enim in laboris reprehenderit proident deserunt qui quis duis ea deserunt exercitation. Magna occaecat excepteur cupidatat sint ex aute ea ullamco.\\r\\n\",\"registered\":\"2015-02-26T07:45:54 +06:00\",\"latitude\":-64.781371,\"longitude\":-51.928241,\"tags\":[\"ut\",\"aliqua\",\"sint\",\"ullamco\",\"deserunt\",\"velit\",\"veniam\"],\"friends\":[{\"id\":0,\"name\":\"Bradley Hampton\"},{\"id\":1,\"name\":\"Hodge Patrick\"},{\"id\":2,\"name\":\"Rachael Ashley\"}],\"greeting\":\"Hello, Vega Atkins! You have 7 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b3694632718c3668be4f\",\"index\":20,\"guid\":\"bd60e5ed-7f6d-48b5-bfff-89a709d15aab\",\"isActive\":false,\"balance\":\"$2,590.38\",\"picture\":\"http://placehold.it/32x32\",\"age\":31,\"eyeColor\":\"brown\",\"name\":\"Bowen Byers\",\"gender\":\"male\",\"commeowy\":\"KANGLE\",\"email\":\"bowenbyers@kangle.com\",\"phone\":\"+1 (802) 513-3093\",\"address\":\"494 Ross Street, Avalon, New Jersey, 7260\",\"about\":\"Pariatur in amet ea aliquip in amet pariatur in eu elit ullamco quis aliquip amet. Irure nisi ea do adipisicing ea. Adipisicing dolor nisi enim magna Lorem dolore eiusmod minim est.\\r\\n\",\"registered\":\"2015-04-02T04:04:25 +05:00\",\"latitude\":20.48846,\"longitude\":4.101133,\"tags\":[\"ad\",\"ipsum\",\"reprehenderit\",\"dolore\",\"nulla\",\"do\",\"aliqua\"],\"friends\":[{\"id\":0,\"name\":\"Pacheco Lopez\"},{\"id\":1,\"name\":\"Barnett Lyons\"},{\"id\":2,\"name\":\"Eva Sanders\"}],\"greeting\":\"Hello, Bowen Byers! You have 6 unread messages.\",\"favoriteFruit\":\"banana\"},{\"_id\":\"5769b369f71f22f28dbc8c4b\",\"index\":21,\"guid\":\"a85f3daf-47dd-45f4-8f4c-bbd2cfbb1ea3\",\"isActive\":true,\"balance\":\"$2,117.89\",\"picture\":\"http://placehold.it/32x32\",\"age\":39,\"eyeColor\":\"brown\",\"name\":\"Leonard Gordon\",\"gender\":\"male\",\"commeowy\":\"JUNIPOOR\",\"email\":\"leonardgordon@junipoor.com\",\"phone\":\"+1 (919) 459-2889\",\"address\":\"156 Leonora Court, Frizzleburg, Maine, 7193\",\"about\":\"Consectetur consequat sit aliqua exercitation id qui ullamco ea id ea tempor aute elit in. Occaecat ullamco laboris ut excepteur anim tempor aliquip in amet reprehenderit ut esse exercitation. Consequat anim nisi nulla ipsum eu elit esse proident cillum elit occaecat enim incididunt. Nostrud non cillum exercitation aliquip officia in quis minim mollit officia. Qui exercitation laborum occaecat sit non velit nostrud laborum sit exercitation do ipsum. Amet amet sit deserunt aute nulla non fugiat. Adipisicing ullamco veniam ullamco ut irure est pariatur aute.\\r\\n\",\"registered\":\"2015-09-07T10:43:34 +05:00\",\"latitude\":-47.994191,\"longitude\":148.834088,\"tags\":[\"est\",\"ipsum\",\"quis\",\"ullamco\",\"laborum\",\"veniam\",\"amet\"],\"friends\":[{\"id\":0,\"name\":\"Hayes Mcdonald\"},{\"id\":1,\"name\":\"Edith Levy\"},{\"id\":2,\"name\":\"Madge Roach\"}],\"greeting\":\"Hello, Leonard Gordon! You have 10 unread messages.\",\"favoriteFruit\":\"banana\"}]";

}
