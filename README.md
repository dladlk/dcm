# DELIS - Catalogue Manager



### JAXB parsing speed test

Repeat each size 5 times, calculate average duration of 4 last attempts.

With -Xmx10 = max 10 mb for java heap

Lines | Size, MB | Duration, ms | Lines/sec
---: | ---: | ---: | ---:
2 | 0 | 6 | 333
7848 | 10 | 622 | 12617
39266 | 50 | 2481 | 15827
78539 | 100 | 4820 | 16294
392719 | 500 | 25152 | 15614

### JAXB parsing and serialization speed test

Repeat each size 5 times, calculate average duration of 4 last attempts.

With -Xmx10m = max 10 mb for java heap

Lines | Size, MB | Duration, ms | Lines/sec
---: | ---: | ---: | ---:
2 | 0 | 9 | 222
7848 | 10 | 664 | 11819
39266 | 50 | 2815 | 13949
78539 | 100 | 5838 | 13453
392719 | 500 | 28741 | 13664

With -Xmx100m = max 100 mb for java heap

Lines | Size, MB | Duration, ms | Lines/sec
---: | ---: | ---: | ---:
2 | 0 | 9 | 222
7848 | 10 | 522 | 15034
39266 | 50 | 2626 | 14953
78539 | 100 | 5263 | 14923
392719 | 500 | 26740 | 14687

### XSLT transformation speed test

Repeat each size 5 times, calculate average duration of 4 last attempts.

With -Xmx3000m (with -Xmx10m fails already on 1 MB XML, with -Xmx256m fails on 100 MB XML, with -Xmx1024m fails on 500m)

Lines | Size, MB | Duration, ms | Lines/sec
---: | ---: | ---: | ---:
2 | 0 | 15 | 133
7848 | 10 | 862 | 9104
39266 | 50 | 3365 | 11669
78539 | 100 | 6988 | 11239
392719 | 500 | 38439 | 10217

With -Xmx1500m

Lines | Size, MB | Duration, ms | Lines/sec
---: | ---: | ---: | ---:
2 | 0 | 19 | 105
7848 | 10 | 746 | 10520
39266 | 50 | 3259 | 12048
78539 | 100 | 6544 | 12002
392719 | 500 | 49313 | 7964
