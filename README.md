# NAME

`jrunlist` operates chromosome runlist files

## SYNOPSIS

```
Usage: <main class> [options] [command] [command options]
  Options:
    --help, -h
       Print this help and quit
       Default: false
  Commands:
    combine      Combine multiple sets of runlists in a yaml file.
                It's expected that the YAML file is --mk.
                Otherwise this command will make no effects.
      Usage: combine [options] <infile>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    compare      Compare 2 YAML files.
                Only the *first* file can contain multiple sets of runlists.
      Usage: compare [options] <infile1> <infile2>
        Options:
          --op
             operations: intersect, union, diff or xor.
             Default: intersect
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    cover      Output covers on chromosomes.
                Like `command combine`, but <infiles> are chromosome positions.
                I:1-100
                I(+):90-150      Strands will be omitted.
                S288c.I(-):190-200      Species names will be omitted.
      Usage: cover [options] <infiles>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    genome      Convert chr.size to runlists
      Usage: genome [options] <infile>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    merge      Merge runlist yaml files
      Usage: merge [options] <infiles>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.

    some      Extract some records from a runlist yaml file
      Usage: some [options] <infile> <list.file>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.

    span      Operate spans in a YAML file.
        List of operations
                cover:  a single span from min to max;
                holes:  all the holes in runlist;
                trim:   remove N integers from each end of each span of runlist;
                pad:    add N integers from each end of each span of runlist;
                excise: remove all spans smaller than N;
                fill:   fill in all holes smaller than or equals to N.
      Usage: span [options] <infile>
        Options:
          --number, -n
             Apply this number to trim, pad, excise or fill.
             Default: 0
          --op
             operations: cover, holes, trim, pad, excise or fill.
             Default: cover
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    split      Split a runlist yaml file
      Usage: split [options] <infile>
        Options:
          --outdir, -o
             Output location. [stdout] for screen.
             Default: .
          --suffix, -s
             Extension of output files.
             Default: .yml

    stat      Coverage on chromosomes for runlists
      Usage: stat [options] <chr.size> <infile>
        Options:
          --all
             Only write whole genome stats.
             Default: false
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

    statop      Coverage on chromosomes for one YAML crossed another
                Only the *first* file can contain multiple sets of runlists.
      Usage: statop [options] <chr.size> <infile1> <infile2>
        Options:
          --all
             Only write whole genome stats.
             Default: false
          --base, -b
             basename of infile2
          --op
             operations: intersect, union, diff or xor.
             Default: intersect
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false

```

## DESCRIPTION

This Java class is ported from the Perl module `App::RL`.

## REQUIREMENTS

Oracle/Open JDK 1.7 or higher.

```
$ cd benchmark
$ bash run.sh
==> jrunlist
       11.33 real        15.37 user         0.64 sys
1397743616  maximum resident set size
         0  average shared memory size
         0  average unshared data size
         0  average unshared stack size
    352760  page reclaims
         0  page faults
         0  swaps
         0  block input operations
         6  block output operations
         0  messages sent
         0  messages received
         5  signals received
         0  voluntary context switches
     18048  involuntary context switches
==> App::RL
      352.19 real       350.19 user         1.60 sys
 115826688  maximum resident set size
         0  average shared memory size
         0  average unshared data size
         0  average unshared stack size
     42417  page reclaims
         0  page faults
         0  swaps
         0  block input operations
         6  block output operations
         0  messages sent
         0  messages received
        20  signals received
        56  voluntary context switches
     55700  involuntary context switches
```

## AUTHOR

Qiang Wang &lt;wang-q@outlook.com&gt;

## COPYRIGHT AND LICENSE

This software is copyright (c) 2016 by Qiang Wang.

This is free software; you can redistribute it and/or modify it under the same terms as the Perl 5
programming language system itself.
