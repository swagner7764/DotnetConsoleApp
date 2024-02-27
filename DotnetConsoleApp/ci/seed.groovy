job('DotnetConsoleApp/dotnet-compile'){ 	
	description 'Compile application'
	label('Windows')
	scm {
        	github('swagner7764/DotnetConsoleApp', 'master')
    	}
  	steps{
      		powerShell 'nuget restore "$ENV:WORKSPACE"'
		shell 'echo Containerizing application via Docker'
		
   	}    
  	publishers {
        	downstream 'DotnetConsoleApp/dotnet-containerize', 'SUCCESS'
   	}
}

job('DotnetConsoleApp/dotnet-containerize'){
  	description 'Dockerize application'
    steps{
       	shell 'echo Containerizing application via Docker'
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
