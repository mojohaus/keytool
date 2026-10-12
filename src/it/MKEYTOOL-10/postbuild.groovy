File target = new File( basedir, "target" )
assert target.isDirectory() : target.getAbsolutePath() + " file is missing or is not a directory."

File keystore = new File( target, "keystore.jks" )
assert keystore.isFile() : keystore.getAbsolutePath() + " file is missing or a directory."
