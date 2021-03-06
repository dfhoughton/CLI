dfh.cli
=======

A small project to facilitate writing CLI programs in Java. The aim is to provide a terse, declarative means of specifying commands, constraints among commands, usage information, and so forth such that a particular CLI program isn't cluttered with the argument parsing code and you can easily find the logic of the program itself. Also, you needn't spend much time writing the parsing code, you just type a terse *declaration* of how it is to occur and get on with things.

This project is inspired by Perl modules such as [Getopt::Long::Descriptive][getoptlong]. It was also inspired by my dislike for the verbosity of [Apache Commons CLI][commonscli], whose object-oriented style fits nicely in the Java paradigm but I find forces me to write too much command parsing code to the detriment of rapid prototyping and code readability. This library doesn't parse as wide a range of option types as either of these, but it gives you your long options -- `--help` -- and your short, POSIX options -- `-xzvf file`. I find this enough.

And yes, there are now many Java alternatives to Commons CLI, though embarrassingly I didn't know that when I started writing this.

David F. Houghton
4 November 2011

example
-------

Here's an example of the code in use in an actual program:

    import dfh.cli.Cli;
    import dfh.cli.Cli.Opt;
    import dfh.cli.rules.Range;

    public class Foo {

       public static void main(String[] args) {
          Object[][][] spec = {
                { { Opt.NAME, Foo.class.getName() } },
                { { Opt.ARGS } },
                { {             
                      Opt.USAGE,                
                      "slice training data into n folds, training on remainder and testing on each in turn" } },//
                { { Opt.VERSION, 1 } },
                { { "folds", 'f', Integer.class, 10 },
                      { "number of sections to slice the data into", "n" },
                      { Range.greaterOrEq(2) } },
                {               
                      { "cutoff", 'c', Integer.class, 3 },
                      {                         
                            "satisfaction level less than which is \"unsatisfactory\"",
                            "threshold" }, { Range.excl(1, 5) } },
                {               
                      { "balance", 'b', Boolean.class, true },
                      { "whether to resample data to even the odds of each class at each stage of cascade" } },//
                { { "dump", 'd' }, { "whether to dump raw data as .arff file" } },
                {               
                      { "binary", String.class },
                      {                         
                            "whether to dump .arff files for first fold cascade training",
                            "file name base" } }, };            
          Cli p = new Cli(spec);
          p.parse(args);
          int folds = p.integer("folds"), cutoff = p.integer("cutoff");
          boolean doDump = p.bool("dump"), doBalance = p.bool("balance");
          String nfoldDump = p.string("binary");
          ...

And here's the usage information produced with the `--help` flag:

    USAGE: Foo [options]

      slice training data into n folds, training on remainder and testing on each in
      turn

        --folds -f   <n>               number of sections to slice the data into;
                                       value must be >= 2; default: 10
        --cutoff -c  <threshold>       satisfaction level less than which is
                                       "unsatisfactory"; value must be in interval
                                       (1, 5); default: 3
        --balance -b                   whether to resample data to even the odds of
                                       each class at each stage of cascade; default:
                                       true
        --dump -d                      whether to dump raw data as .arff file
        --binary     <file name base>  whether to dump .arff files for first fold
                                       cascade training

        --version -v                   print Foo version
        --help -? -h                   print usage information

Full Documentation
------------------

For the complete, up-to-date documentation, see [my site][docs].

License
-------
This software is distributed under the terms of the FSF Lesser Gnu
Public License (see lgpl.txt).

[getoptlong]: https://metacpan.org/module/Getopt::Long::Descriptive
[commonscli]: http://commons.apache.org/cli/
[docs]: http://dfhoughton.org/cli/
