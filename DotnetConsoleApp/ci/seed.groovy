job('dotnet-checkout') {   
  	description 'Check out application from github'
    scm {
        github('swagner7764/DotnetConsoleApp', 'master')
    }
  	steps{
     	shell 'echo Checking out application from github'
    }      
    publishers {
        downstream 'dotnet-compile', 'SUCCESS'
    }    
}

job('dotnet-compile'){ 
	description 'Compile application'  
  	steps{
      	shell 'echo Running MSBuild'
   	}    
  	publishers {
        downstream 'dotnet-containerize', 'SUCCESS'
   }
}

job('dotnet-containerize'){
  	description 'Dockerize application'
    steps{
       	shell 'echo Containerizing application via Docker'
    }    
  	publishers {
        downstream 'dotnet-deploy-container', 'SUCCESS'
  	}
}

job('dotnet-deploy-container') {
    description 'Deploy Container to AWS ECR'    
    steps{
      	shell 'echo Deploying container to AWS ECR'
   	}
}

deliveryPipelineView('dotnet delivery pipeline') {
    showAggregatedPipeline true
    enableManualTriggers true
    pipelineInstances 5
    pipelines {
        component('dotnet delivery pipeline', 'dotnet-checkout')
    }
}