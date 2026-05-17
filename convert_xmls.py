import os
import glob
import re
LAYOUT_DIR = 'app/src/main/res/layout/'
for file_path in glob.glob(os.path.join(LAYOUT_DIR, '*.xml')):
    with open(file_path, 'r') as f:
        content = f.read()
    original = content
    # Replace white backgrounds with push_black on root elements safely
    # (A bit tricky via regex, let's just do known generic text color replacements)
    content = content.replace('@color/black', '@color/push_text_primary')
    content = content.replace('@color/primaryTextColor', '@color/push_text_primary')
    content = content.replace('?android:attr/textColorPrimary', '@color/push_text_primary')
    content = content.replace('?attr/colorPrimaryDark', '@color/push_purple_900')
    content = content.replace('?attr/colorPrimary', '@color/push_purple_500')
    # Specific backgrounds
    content = content.replace('@color/app_background', '@color/push_black')
    content = content.replace('@color/white', '@color/push_white')
    content = content.replace('@color/whatsapp_green', '@color/push_purple_500')
    content = content.replace('@color/whatsapp_green_tea', '@color/push_purple_400')
    # Text styles
    content = content.replace('textColor="#000000"', 'textColor="@color/push_text_primary"')
    # Update app cards
    content = content.replace('MaterialCardView', 'com.google.android.material.card.MaterialCardView\n        app:cardBackgroundColor="@color/push_card"\n        app:strokeColor="@color/push_border"\n        app:strokeWidth="1dp"')
    # Fix the double declaration of MaterialCardView if happened natively:
    content = content.replace('com.google.android.material.card.MaterialCardView\n        app:cardBackgroundColor="@color/push_card"\n        app:strokeColor="@color/push_border"\n        app:strokeWidth="1dp"', 'MaterialCardView')
    content = content.replace('<com.google.android.material.card.MaterialCardView', '<com.google.android.material.card.MaterialCardView\n        app:cardBackgroundColor="@color/push_card"\n        app:strokeColor="@color/push_border"\n        app:strokeWidth="1dp"')
    if original != content:
        with open(file_path, 'w') as f:
            f.write(content)
print("Conversion applied to layout XML files.")
