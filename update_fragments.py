import os, glob, re
LAYOUT_DIR = 'app/src/main/res/layout/'
for file_path in glob.glob(os.path.join(LAYOUT_DIR, 'fragment_*.xml')):
    with open(file_path, 'r') as f:
        content = f.read()
    # The background of fragment root view needs to be explicit push_black
    if 'android:background=' not in content:
        content = re.sub(r'xmlns:tools="http://schemas.android.com/tools"',
                         'xmlns:tools="http://schemas.android.com/tools"\n    android:background="@color/push_black"',
                         content, 1)
    with open(file_path, 'w') as f:
        f.write(content)
