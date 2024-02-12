job('Dotnet Console App/dotnet-checkout') {   
  	description 'Check out application from github'
    scm {
        github('swagner7764/DotnetConsoleApp', 'master')
    }
  	steps{
     	shell 'echo Checking out application from github'
    }      
    publishers {
        downstream 'Dotnet Console App/dotnet-compile', 'SUCCESS'
    }    
}

job('Dotnet Console App/dotnet-compile'){ 
	description 'Compile application'  
  	steps{
      	shell 'echo Running MSBuild'
   	}    
  	publishers {
        downstream 'Dotnet Console App/dotnet-containerize', 'SUCCESS'
   }
}

job('Dotnet Console App/dotnet-containerize'){
  	description 'Dockerize application'
    steps{
       	shell 'echo Containerizing application via Docker'
    }    
  	publishers {
        downstream 'Dotnet Console App/dotnet-deploy-container', 'SUCCESS'
  	}
}

job('Dotnet Console App/dotnet-deploy-container') {
    description 'Deploy Container to AWS ECR'    
    steps{
      	shell 'echo Deploying container to AWS ECR'
   	}
}

deliveryPipelineView('Dotnet Console App/dotnet delivery pipeline') {
    showAggregatedPipeline true
    enableManualTriggers true
    pipelineInstances 5
    pipelines {
        component('Dotnet Console App/dotnet delivery pipeline', 'Dotnet Console App/dotnet-checkout')
    }
}