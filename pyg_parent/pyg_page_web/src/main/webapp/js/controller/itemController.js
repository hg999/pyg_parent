//��Ʒ��ϸҳ�����Ʋ㣩 
app.controller('itemController',function($scope,$http){ 
	$scope.specificationItems={};//�洢�û�ѡ��Ĺ��
 	//�������� 
 	$scope.addNum=function(x){  
	$scope.num=$scope.num+x; 
 	 	if($scope.num<1){ 
 	 	 	$scope.num=1; 
 	 	} 
 	}

	//�û�ѡ����
	$scope.selectSpecification=function(key,value){
	   $scope.specificationItems[key]=value;   
		searchSku();//��ѯSKU		   
	}
	//�ж�ĳ����Ƿ�ѡ��
	$scope.isSelected=function(key,value){
	   if($scope.specificationItems[key]==value){
		  return true;
	   }else{
		  return false;
	   }  
	}

	$scope.sku={};//��ǰѡ���SKU
	//����Ĭ��SKU
	$scope.loadSku=function(){
		$scope.sku=skuList[0];
		$scope.specificationItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
	}	
	
	//ƥ�����������Ƿ����
	matchObject=function(map1,map2){
	   for(var k in map1){
		  if(map1[k]!=map2[k]){
			 return false;
		  }        
	   }
	   for(var k in map2){
		  if(map2[k]!=map1[k]){
			 return false;
		  }        
	   }     
	   return true;
	}
	
	//���ݹ���ѯsku
	searchSku=function(){
	   for(var i=0;i<skuList.length;i++){
		   if(matchObject(skuList[i].spec ,$scope.specificationItems)){
			  $scope.sku=skuList[i];
			  return ;
		   }       
	   }
	   $scope.sku={id:0,title:'-----',price:0};
	}
	
	//�����Ʒ�����ﳵ
	$scope.addToCart=function(){
	   //alert('SKUID:'+$scope.sku.id );  
		$http.get('http://localhost:9107/cart/addGoodsToCartList.do?itemId='+$scope.sku.id +'&num='+$scope.num,{'withCredentials':true}).success(
			function(response){
				if(response.success){
					location.href='http://localhost:9107/cart.html';//��ת�����ﳵҳ��
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	
	
	
}); 