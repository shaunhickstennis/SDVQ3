#!/usr/bin/env python3
"""Static APK inventory. Never executes code from the APK or extracts game assets."""
import argparse, hashlib, json, pathlib, zipfile

def inspect(path):
    p = pathlib.Path(path)
    with zipfile.ZipFile(p) as z:
        entries = z.infolist()
        names = [x.filename for x in entries]
        libraries = sorted(x for x in names if x.startswith('lib/') and x.endswith('.so'))
        return {
            'file': p.name,
            'sha256': hashlib.file_digest(p.open('rb'), 'sha256').hexdigest(),
            'bytes': p.stat().st_size,
            'zip_entries': len(entries),
            'architectures': sorted(set(x.split('/')[1] for x in libraries)),
            'managed_assemblies': sorted(x for x in names if x.endswith('.dll')),
            'notable_native_libraries': [x for x in libraries if any(k in x.lower() for k in ('monogame','stardew','apkvision','mono','unity'))],
            'dex_files': sorted(x for x in names if x.startswith('classes') and x.endswith('.dex')),
            'has_manifest': 'AndroidManifest.xml' in names,
            'has_xnb_assets': any(x.lower().endswith('.xnb') for x in names),
            'xnb_count': sum(x.lower().endswith('.xnb') for x in names),
        }

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('apk')
    parser.add_argument('--output', default='apk-inventory.json')
    args = parser.parse_args()
    result = inspect(args.apk)
    pathlib.Path(args.output).write_text(json.dumps(result, indent=2) + '\n')
    print(f"Wrote {args.output} — {result['zip_entries']} entries; {result['xnb_count']} XNB assets")
