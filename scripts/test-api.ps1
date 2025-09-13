$requestBody = @'
{
  "skills": ["python", "sql"],
  "location": "rural",
  "interestSector": "IT",
  "categoryRural": 1,
  "categoryTribal": 0,
  "pastParticipation": 0
}     
'@

try {
    Write-Host "Testing health endpoint..."
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/health" -Method GET
    Write-Host "Health check result: $healthResponse"
    
    Write-Host "`nTesting recommendation endpoint..."
    $response = Invoke-RestMethod -Uri "http://localhost:8080/recommend" -Method POST -Body $requestBody -ContentType "application/json"
    
    Write-Host "`nRecommendation result:"
    $response | ConvertTo-Json -Depth 10
    
    Write-Host "`nAPI test completed successfully!"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Make sure the Spring Boot application is running on port 8080"
}

Read-Host "`nPress Enter to continue..."