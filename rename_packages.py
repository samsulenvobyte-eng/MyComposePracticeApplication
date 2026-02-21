import os
import re

PROJECT_DIR = r"E:\AAA\Codes\R and D\MyComposePracticeApplication\app\src\main\java\com\example\mypracticeapplication"
BASE_PACKAGE = "com.example.mypracticeapplication"

class_moves = {} # mapping of old_import -> new_import

# Pass 1: Update package declarations and build the mapping
for root, _, files in os.walk(PROJECT_DIR):
    for file in files:
        if file.endswith(".kt"):
            filepath = os.path.join(root, file)
            
            # calculate expected package from path
            rel_path = os.path.relpath(root, PROJECT_DIR)
            if rel_path == ".":
                expected_package = BASE_PACKAGE
            else:
                expected_package = f"{BASE_PACKAGE}.{rel_path.replace(os.sep, '.')}"
            
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
                
            # Find old package
            package_match = re.search(r'^package\s+([\w.]+)', content, re.MULTILINE)
            if package_match:
                old_package = package_match.group(1)
                
                # Update package declaration
                if old_package != expected_package:
                    new_content = re.sub(r'^package\s+[\w.]+', f"package {expected_package}", content, count=1, flags=re.MULTILINE)
                    with open(filepath, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    
                    # Store mapping for imports update
                    # To be safe, we map all classes in this file
                    # We can use simple regexes to find top-level classes/interfaces/objects/functions
                    # Or simpler: we assume the file name (without .kt) is the primary class/function exported
                    file_base = file[:-3]
                    old_import = f"{old_package}.{file_base}"
                    new_import = f"{expected_package}.{file_base}"
                    class_moves[old_import] = new_import
                    
                    # also map wildcard imports just in case
                    class_moves[f"{old_package}.*"] = f"{expected_package}.*"
            
# specific known moves based on the directory moves we just did
# the old UI package was com.example.mypracticeapplication.ui.screens...
# we need to build a more comprehensive replace list to handle imports safely.
# A simpler approach for the second pass: just do a global replace of known prefixes

PREFIX_REPLACEMENTS = [
    ("com.example.mypracticeapplication.ui.screens.notification.data", "com.example.mypracticeapplication.data.notification"),
    ("com.example.mypracticeapplication.ui.screens.notification.domain", "com.example.mypracticeapplication.domain.notification"),
    ("com.example.mypracticeapplication.ui.screens.notification.presentation", "com.example.mypracticeapplication.presentation.notification"),
    ("com.example.mypracticeapplication.ui.screens.", "com.example.mypracticeapplication.presentation."),
    ("com.example.mypracticeapplication.ui.theme", "com.example.mypracticeapplication.presentation.theme"),
    ("com.example.mypracticeapplication.model", "com.example.mypracticeapplication.domain.model"),
    ("com.example.mypracticeapplication.components", "com.example.mypracticeapplication.presentation.components"),
    ("com.example.mypracticeapplication.navigation", "com.example.mypracticeapplication.presentation.navigation"),
]

# Pass 2: Update imports in all files
for root, _, files in os.walk(PROJECT_DIR):
    for file in files:
        if file.endswith(".kt"):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            modified = False
            lines = content.split('\n')
            for i, line in enumerate(lines):
                if line.startswith("import "):
                    for old_p, new_p in PREFIX_REPLACEMENTS:
                        if old_p in line:
                            lines[i] = line.replace(old_p, new_p)
                            modified = True
            
            if modified:
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write('\n'.join(lines))

print("Packages and specific imports updated successfully")
