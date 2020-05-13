# DT176G Reactive Programming
## Lab assignment part 2

Author: Anders Jensen-Urstad <anje0901@student.miun.se>

(Oförändrad sedan inlämning 1 p.g.a. ännu ingen feedback på inlämning 1, och nätverksdelen
var hursomhelst redan "klar")

Programmet återanvänder mycket från det ritprogram jag byggde under kursen DT062G.
Lite oklart hur "So "everything" should be an Observable." ska tolkas. Jag menar, en metod
som Circle.draw():

```
public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    // Parent class sets color
    super.draw(g2);
    // etc...
```

...skulle kunna "wrappas" så här:

```
public void draw(Graphics g) {
    Observable.just(g).subscribe((gfxfoo) -> {
        Graphics2D g2 = (Graphics2D)gfxfoo;
        super.draw(gfxfoo);
        // etc...
```

...men är det meningsfullt? Tveksamt? Men det är säkert saker jag missat och möjligheter
jag inte använder mig av, så tips på sådant och vad som (inte) borde vara reaktivt
mottages tacksamt!

Det kändes mer cleant att separera klient och server, så de två programmen är PaintClient
och PaintServer. Se nedan för bygginstruktioner. Jag har dock för enkelhetens skull också
inkluderat två färdiga JAR-filer som det förhoppningsvis bara är att köra:

```
java -jar server.jar
java -jar client.jar
```

Serverporten 23999 (TCP) är hårdkodad. [Kryonet](https://github.com/EsotericSoftware/kryonet)
används för nätverksdelen.

För att göra det smidigt, och för att jag tyckte det vore kul att kunna se vad andra
klienter ritar "live" (och inte bara när musknappen släpps), har jag ett mellanlager i form
av klassen DrawingEvent. Den innehåller informationen som behövs för att rita en Drawing, eller
uppdatera en befintlig Drawing (dvs då MOUSE_DRAGGED sker). För att det senare ska funka får varje Drawing
ett unikt ID (klientens slumpmässiga UUID + serienummer). Prestandan blir ju lite sådär eftersom varje
mousePressed och mouseDragged orsakar ett individuellt DrawingEvent som skickas till servern (och sen broadcastas
till alla klienter), men bra prestanda var ju inget krav.

## Build instructions

This is a [Maven](https://maven.apache.org/) project to make it easy to use the
project with any IDE that supports Maven (or without an IDE!).

Java SE >= 10 is required.

### With Maven

Build:

```
cd dt176g-anje0901-jpaint
mvn package
```

Run:

```
java -jar target/dt176g-anje0901-jpaint-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### With Eclipse
* File -> Import -> Maven -> Existing Maven Projects
* Choose directory from `git clone` in Step 1
* Make sure `pom.xml` is selected
* Press Finish

### With IntelliJ IDEA
From Welcome screen:

* Choose Import Project
* Navigate to cloned directory
* Select pom.xml
* Press OK
* Choose "Open as Project"
* Press Next, Next, Next, Finish

## Development environment
* Ubuntu Linux 18.04.1
* OpenJDK 10.0.2
* Apache Maven 3.5.2
* IntelliJ IDEA 2018.2.5
