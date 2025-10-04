# PowerShell script to fix Git merge conflicts
Write-Host "Fixing Git merge conflicts..."

# Get all files with merge conflicts
$conflictFiles = Get-ChildItem -Recurse -File | Where-Object { 
    (Get-Content $_.FullName -Raw) -match "<<<<<<< HEAD|=======|>>>>>>> "
}

Write-Host "Found $($conflictFiles.Count) files with conflicts"

foreach ($file in $conflictFiles) {
    Write-Host "Processing: $($file.FullName)"
    
    $content = Get-Content $file.FullName -Raw
    
    # Remove merge conflict markers and keep the HEAD version
    $content = $content -replace "<<<<<<< HEAD\s*\n", ""
    $content = $content -replace "=======.*?\n", ""
    $content = $content -replace ">>>>>>> [a-f0-9]+\s*\n", ""
    
    # Clean up extra newlines
    $content = $content -replace "\n\s*\n\s*\n", "`n`n"
    
    Set-Content -Path $file.FullName -Value $content -NoNewline
}

Write-Host "Merge conflicts fixed!"
Write-Host "Run: git add . && git commit -m 'Fix all merge conflicts'"
