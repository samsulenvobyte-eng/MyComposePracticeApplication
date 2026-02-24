$projectDir = "E:\AAA\Codes\R and D\MyComposePracticeApplication\app\src\main\java\com\example\mypracticeapplication"
$basePackage = "com.example.mypracticeapplication"

$prefixReplacements = @(
    @("com.example.mypracticeapplication.ui.screens.notification.data", "com.example.mypracticeapplication.data.notification"),
    @("com.example.mypracticeapplication.ui.screens.notification.domain", "com.example.mypracticeapplication.domain.notification"),
    @("com.example.mypracticeapplication.ui.screens.notification.presentation", "com.example.mypracticeapplication.presentation.notification"),
    @("com.example.mypracticeapplication.ui.screens.", "com.example.mypracticeapplication.presentation."),
    @("com.example.mypracticeapplication.ui.theme", "com.example.mypracticeapplication.presentation.theme"),
    @("com.example.mypracticeapplication.model", "com.example.mypracticeapplication.domain.model"),
    @("com.example.mypracticeapplication.components", "com.example.mypracticeapplication.presentation.components"),
    @("com.example.mypracticeapplication.navigation", "com.example.mypracticeapplication.presentation.navigation")
)

Get-ChildItem -Path $projectDir -Recurse -Filter "*.kt" | ForEach-Object {
    $file = $_.FullName
    $dirName = $_.DirectoryName
    
    # Calculate relative path manually
    if ($dirName -eq $projectDir) {
        $expectedPackage = $basePackage
    } else {
        $relPath = $dirName.Substring($projectDir.Length + 1)
        $expectedPackage = "$basePackage." + ($relPath -replace "\\", ".")
    }
    
    $content = Get-Content -Path $file -Raw -Encoding UTF8
    $modified = $false
    
    # 1. Update Package
    if ($content -match "(?m)^package\s+([\w.]+)") {
        $oldPackage = $Matches[1]
        if ($oldPackage -ne $expectedPackage) {
            $content = $content -replace "(?m)^package\s+[\w.]+", "package $expectedPackage"
            $modified = $true
        }
    }
    
    # 2. Update Imports
    $lines = $content -split "`r`n|`n"
    for ($i = 0; $i -lt $lines.Length; $i++) {
        $line = $lines[$i]
        if ($line.Trim().StartsWith("import ")) {
            foreach ($pair in $prefixReplacements) {
                $oldP = $pair[0]
                $newP = $pair[1]
                if ($line.Contains($oldP)) {
                    $lines[$i] = $line.Replace($oldP, $newP)
                    $modified = $true
                }
            }
        }
    }
    
    if ($modified) {
        $newContent = $lines -join "`n"
        Set-Content -Path $file -Value $newContent -Encoding UTF8
    }
}
Write-Output "PowerShell package update script completed."
