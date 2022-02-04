# Adjustments for ONR/netrunner 1996

All ONR cards will have their titles prefaced by "ONR ", like `ONR Accounts Receivable`. 

All ONR cards have unique subtypes, like `onr-asset` or `onr-black-ice`. 

As I work, I'm trying to put cards in baskets: easy to implement, things that will take work, and things that require an actual game system rewrite.

Implementation status of ONR cards can be found in 'implement' and 'unimplemented' text files, as I slowly work through them (in order).

There are several game systems that need to be changed/modified/added for onr to work. These are:
* counters get hosted all over the place, and lots of cards have their own unique counters (every single virus, lots of ice, etc)
* lots of cards create permanent floating effects
* you can pay agenda points (different to forfieting agendas)
* some cards disable actions (as a floating effect) until a cost is paid - interaction needs to persist even if that card dissapears
* purging requires you to forgo future actions. You can purge in any ability window.
* Other abilities on both sides will also require players to forgo actions
    * the solution for this is to do the following
    * There's a "actions owed/to forgo" counter for each player
    * If the counter is positive, no basic or click actions can be taken by that player except forgoing an action
* The ONR trace system is different - the corp can pay credits up to the trace strength, and the runner needs link cards to interact with traces
* Bad publicity does not give credits per run - it instead makes the corp lose at 7
* Regions (and certain other cards) require the corp to have the rez credits handy already, and are rezzed when installed

# ONR Implementation status
There are currently 61/580 cards fully implemented, and 6/580 cards partially implemented.

# Why

I thought it would be fun.

# Android: Netrunner in the browser

[![Build status](https://circleci.com/gh/mtgred/netrunner/tree/master.svg?style=shield)](https://circleci.com/gh/mtgred/netrunner)

Hosted at [http://www.jinteki.net](http://www.jinteki.net). [Example of gameplay](https://www.youtube.com/watch?v=cnWudnpeY2c).

![screenshot](http://i.imgur.com/xkxOMHc.jpg)

## Card implementation status

[Card rules implementation status](https://docs.google.com/spreadsheets/d/1ICv19cNjSaW9C-DoEEGH3iFt09PBTob4CAutGex0gnE/pubhtml)

## Development

### Quickstart

Install [Leiningen](https://leiningen.org/),
[NodeJS](https://nodejs.org/en/download/package-manager/#macos) and
[MongoDB](https://docs.mongodb.com/manual/installation/).

This project runs on Java 8. If you're on OSX or Linux, we recommend using
[jenv](https://github.com/jenv/jenv/blob/master/README.md) to manage your java environment.

You can check your setup by running:

    $ lein version
    Leiningen 2.9.6 on Java 16.0.1 OpenJDK 64-Bit Server VM

Your exact version numbers below may vary, but we require Java 1.8+.

Populate the database and create indexes using:

    $ lein fetch [--no-card-images]
    1648 cards imported

    $ lein create-indexes
    Indexes successfully created.

You can optionally pass `--no-card-images` if you don't want to download images from
[NetrunnerDB](https://netrunnerdb.com/), as this takes a while. See `lein fetch help`
for further options.

To install frontend dependencies, run:

    $ npm ci
    added 124 packages, and audited 125 packages in 2s

To compile CSS:

    $ npm run css:build
    compiled resources/public/css/netrunner.css

Optionally you can say `npm run watch:css` to watch for changes and automatically
recompile.

Compile ClojureScript frontend:

    $ npm run cljs:build
    [:app] Compiling ...
    [:app] Build completed. (238 files, 0 compiled, 0 warnings, 22.18s)

Finally, launch the webserver and the Clojure REPL:

    $ lein repl
    dev.user=>

and open [http://localhost:1042/](http://localhost:1042/).

### Tests

To run all tests:

    $ lein test
    Ran 2640 tests containing 44704 assertions.
    0 failures, 0 errors.

To run a single test file:

    $ lein test game.cards.agendas-test
    Ran 216 tests containing 3536 assertions.
    0 failures, 0 errors.

Or a single test:

    $ lein test :only game.cards.agendas-test/fifteen-minutes
    Ran 1 tests containing 29 assertions.
    0 failures, 0 errors.

For more information refer to the [development guide](https://github.com/mtgred/netrunner/wiki/Getting-Started-with-Development).

### Further reading

- [Development Tips and Tricks](https://github.com/mtgred/netrunner/wiki/Development-Tips-and-Tricks)
- [Writing Tests](https://github.com/mtgred/netrunner/wiki/Tests)
- "Profiling Database Queries" in `DEVELOPMENT.md`

## License

Jinteki.net is released under the [MIT License](http://www.opensource.org/licenses/MIT).
