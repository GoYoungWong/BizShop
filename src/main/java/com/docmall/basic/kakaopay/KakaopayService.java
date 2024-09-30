package com.docmall.basic.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KakaopayService {

	@Value("${kakaopay.api.secret.key}")
	private String kakaopaysecretkey;
	
	@Value("${cid}")
	private String cid;
	
	@Value("${approvalUrl}")
	private String approval;
	
	@Value("${cancelUrl}")
	private String cancel;
	
	@Value("${failUrl}")
	private String fail;
	
	// 전역변수: 이렇게 따로 뺀 이유는 결제준비요청(ready)도 쓰고 결제승인요청(approve) 여기도 사용해서 따로 뺌
    private String tid;
    
    private String partnerOrderId;
    
    private String partnerUserId;
    
    // 1)경제준비요청(ready)
    public ReadyResponse ready(String partnerOrderId, String partnerUserId, String itemName,
    								int quantity, int totalAmount, int taxFreeAmount, int vatAmount) {
    	
    	// 1) Request header
    	HttpHeaders headers = new HttpHeaders();
    	
    	headers.set("Authorization", "SECRET_KEY " + kakaopaysecretkey);
    	headers.set("Content-Type", "application/json;charset=utf-8");
    	
    	// 2) Request Param
    	ReadyRequest readyRequest = ReadyRequest.builder()
    			.cid(cid)
    			.partnerOrderId(partnerOrderId)
    			.partnerUserId(partnerUserId)
    			.itemName(itemName)
    			.quantity(quantity)
    			.totalAmount(totalAmount)
    			.taxFreeAmount(taxFreeAmount)
    			.vatAmount(vatAmount)
    			.approvalUrl(approval)
    			.cancelUrl(cancel)
    			.failUrl(fail)
    			.build();
    	
    	// 3) Send Reqeust
    	HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest,headers);
    	
    	ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
    			"https://open-api.kakaopay.com/online/v1/payment/ready",
    			entityMap,
    			ReadyResponse.class);
    	
    	ReadyResponse readyResponse = response.getBody();
    	
    	this.tid = readyResponse.getTid(); 
    	
    	this.partnerOrderId = partnerOrderId;
    	this.partnerUserId = partnerUserId;
    	
    	return readyResponse;
    	
    }
    
    // 2) 결제승인요청
    public String approve(String pgToken) { // pgToken: 카카오페이에서 제공하는 결제 승인을 위한 토큰
    	
    	// 1) Request header
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", "SECRET_KEY " + kakaopaysecretkey);
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
    	// 2) Request Param
    	ApproveRequest approveRequest = ApproveRequest.builder()
    			.cid(cid)
    			.tid(tid)
    			.partnerOrderId(partnerOrderId)
    			.partnerUserId(partnerUserId)
    			.pgToken(pgToken)
    			.build();
    	
    	// 3) Send Reqeust
    	HttpEntity<ApproveRequest> entityMap = new HttpEntity<ApproveRequest>(approveRequest, headers);
    	try {
    		ResponseEntity<String> response = new RestTemplate().postForEntity(
    				"https://open-api.kakaopay.com/online/v1/payment/approve",
    				entityMap,
    				String.class);
    		// 승인 결과를 저장한다.
            // save the result of approval
    		String approveResponse = response.getBody();
    		
    		log.info(approveResponse);
    		
    		return approveResponse;
    		
    	}catch(HttpStatusCodeException ex) {
    		return ex.getResponseBodyAsString();
    	}
    	
    }
    
    
    
    
    
    
    
    
    
}
