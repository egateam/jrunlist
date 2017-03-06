# runlist 

`jrunlist` operates chromosome runlist files
`jrunlist`可操作染色体runlist文件

## Compile
   编译

```bash
brew install wang-q/tap/jrunlist
利用homebrew进行下载安装
```

## Example
   例子

Usage: <main class> [options] [command] [command options]
用法：<主体> [选项] [命令] [命令选项]

Options:
选项：
		--help, -h
				Print this help and quit
        Default: false

Commands:
命令：

### 1. combine      
                 Combine multiple sets of runlists in a yaml file.
		             It's expected that the YAML file is --mk.
		             Otherwise this command will make no effects.
		             --在一个yaml文件中，合并多个runlists的集合
      Usage: combine [options] <infile>
        Options:
          --outfile, -o
             Output filename. [stdout] for screen.
          --remove, -r
             Remove 'chr0' from chromosome names.
             Default: false
             
      Example：
      ```bash
      jrunlist combine t/Atha.yml -o stdout
      jrunlist combine t/brca2.yml -o stdout
      ```
      Explanation：
      Atha.yml：
		  ---
		  AT1G01010.1:
  		  1: 3631-3913,3996-4276,4486-4605,4706-5095,5174-5326,5439-5899
		  AT1G01020.1:
  	  	1: 5928-6263,6437-7069,7157-7232,7384-7450,7564-7649,7762-7835,7942-7987,8236-8325,8417-8464,8571-8737
		  AT1G01020.2:
  	  	1: 6790-7069,7157-7450,7564-7649,7762-7835,7942-7987,8236-8325,8417-8464,8571-8737
		  AT2G01008.1:
  	  	2: 1025-1272,1458-1510,1873-2810,3706-5513,5782-5945
		  AT2G01021.1:
 		   	2: 6571-6672
      经过combine后可以得到：
      ---
		  1: "3631-3913,3996-4276,4486-4605,4706-5095,5174-5326,5439-5899,5928-6263,6437-7069,7157-7450,7564-7649,7762-7835,7942-7987,8236-8325,8417-8464,8571-8737"
		  2: "1025-1272,1458-1510,1873-2810,3706-5513,5782-5945,6571-6672"
    
      同理brca2.yml

### 2. compare      
                 Compare 2 YAML files.
		             Only the *first* file can contain multiple sets of runlists.
		             --比较两个YAML文件
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
             
      Example：
      ```bash
      jrunlist compare --op intersect t/intergenic.yml t/repeat.yml -o stdout
             
      

    cover      Output covers on chromosomes.
		           Like `command combine`, but <infiles> are chromosome positions.
		           I:1-100
		           I(+):90-150	 Strands will be omitted.
		           S288c.I(-):190-200	Species names will be omitted.
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
