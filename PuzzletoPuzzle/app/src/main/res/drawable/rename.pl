foreach $_ (@ARGV) {
   my $oldfile = $_;
  my $newfile = "image".$oldfile;
   rename($oldfile, $newfile);
}