"""Package client UI only. Proprietary DAT/SPR and credentials are always excluded."""
import argparse,pathlib,zipfile
p=argparse.ArgumentParser();p.add_argument('--output',required=True);p.add_argument('--exe');a=p.parse_args()
r=pathlib.Path(__file__).resolve().parents[1];out=pathlib.Path(a.output);out.parent.mkdir(parents=True,exist_ok=True)
files=[r/'init.lua',r/'LICENSE',r/'docs/TIBIA860.md']
for name in ['data','modules','layouts','mods']:files.extend(x for x in (r/name).rglob('*') if x.is_file())
with zipfile.ZipFile(out,'w',zipfile.ZIP_DEFLATED) as z:
 for f in files:
  if f.suffix.lower() in {'.dat','.spr','.jks','.keystore'} or 'things' in f.relative_to(r).parts or '.git' in f.parts:continue
  z.write(f,str(f.relative_to(r)))
 if a.exe:z.write(a.exe,'Tibia860.exe')
 z.writestr('data/things/860/IMPORT-ASSETS.txt','Copy your authorized Tibia.dat and Tibia.spr here. Android imports these at first launch.\n')
with zipfile.ZipFile(out) as z:
 assert not any(n.lower().endswith(('.dat','.spr','.jks','.keystore')) for n in z.namelist())
 assert 'init.lua' in z.namelist()
print(out)
