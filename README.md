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

```

## DESCRIPTION

This Java class is ported from the Perl module `App::RL`.

## REQUIREMENTS

Install jintspan to local repository.

```bash
cd ~/Scripts/java/jintspan
mvn install
```

## AUTHOR

Qiang Wang &lt;wang-q@outlook.com&gt;

## COPYRIGHT AND LICENSE

This software is copyright (c) 2016 by Qiang Wang.

This is free software; you can redistribute it and/or modify it under the same terms as the Perl 5
programming language system itself.
