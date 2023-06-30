# samplesheetgen-clj
A Babashka (Clojure) utility for generating samplesheets (CSV) for different platforms from a base samplesheet, designed with Nextflow and nf-core samplesheet practices in mind 


The use case is with generation of path-specific samplesheets starting from a base samplesheet

- Input samplesheet

```csv
sample_id,fastq_1,fastq_2
study-001,001_R1.fastq.gz,001_R2.fastq.gz
study-002,002_R1.fastq.gz,002_R2.fastq.gz
study-003,003_R1.fastq.gz,
```

- Use `samplesheetgen.bb.clj` script (assuming you have babashka installed)

```
$ samplesheetgen.bb.clj csv -i samplesheet_base.csv -o samplesheet.googlestrage.csv -p "gs:my-bucket"
```

- The new samplesheet would look like

```csv
sample_id,fastq_1,fastq_2
study-001,gs:my-bucket/001_R1.fastq.gz,gs:my-bucket/001_R2.fastq.gz
study-002,gs:my-bucket/002_R1.fastq.gz,gs:my-bucket/002_R2.fastq.gz
study-003,gs:my-bucket/003_R1.fastq.gz,
```
