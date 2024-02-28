job('DotnetConsoleApp/dotnet-compile'){ 	
	description 'Compile application'
	label('Windows')
	scm {
        	github('swagner7764/DotnetConsoleApp', 'master')
    	}
  	steps{
      		powerShell 'nuget restore "$ENV:WORKSPACE"'
		msBuild {
	            	msBuildInstallation('MSBuild 2022')
	            	buildFile('${WORKSPACE}/DotnetConsoleApp.sln')	            	
        	}			
   	}    
  	publishers {
        	downstream 'DotnetConsoleApp/dotnet-containerize', 'SUCCESS'
   	}
}

job('DotnetConsoleApp/dotnet-containerize'){
  	description 'Dockerize application'
	customWorkspace('/DotnetConsoleApp/dotnet-compile')
	label('Windows')
    	steps{
		powerShell 'docker build . -t dotnettest -f C:\tools\jenkins-agent\workspace\DotnetConsoleApp\dotnet-compile\DotnetConsoleApp\DockerFile'
	}    
  	publishers {
        	downstream 'DotnetConsoleApp/dotnet-deploy-container', 'SUCCESS'
  	}
}

job('DotnetConsoleApp/dotnet-deploy-container') {
    description 'Deploy Container to AWS ECR'    
    steps{
      	shell 'echo Deploying container to AWS ECR'
   	}
}

deliveryPipelineView('DotnetConsoleApp/dotnet delivery pipeline') {
    showAggregatedPipeline true
    enableManualTriggers true
    pipelineInstances 5
    pipelines {
        component('DotnetConsoleApp/dotnet delivery pipeline', 'DotnetConsoleApp/dotnet-compile')
    }
}
