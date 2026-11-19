import pathlib,struct,sys
root=pathlib.Path(sys.argv[1]);files=list(root.rglob('*.so'));assert files,'APK has no native libraries'
for p in files:
 b=p.read_bytes();assert p.parent.name=='arm64-v8a',p
 assert b[:6]==b'\x7fELF\x02\x01' and struct.unpack_from('<H',b,18)[0]==183,p
 offset=struct.unpack_from('<Q',b,32)[0];size,count=struct.unpack_from('<HH',b,54)
 for n in range(count):
  pos=offset+n*size
  if struct.unpack_from('<I',b,pos)[0]==1:assert struct.unpack_from('<Q',b,pos+48)[0]>=16384,(p,'PT_LOAD alignment <16K')
 print('PASS ARM64 / 16 KiB:',p.name)
