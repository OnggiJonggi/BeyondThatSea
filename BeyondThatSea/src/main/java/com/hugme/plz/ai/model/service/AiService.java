package com.hugme.plz.ai.model.service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.text.StringEscapeUtils;
import com.hugme.plz.ai.model.vo.AiRequest;
import com.hugme.plz.ai.model.vo.Message;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class AiService {

	//ai-mapper.xml에서 property 추출 
	private Properties prop = new Properties();
	public AiService() {
		try {
			ClassPathResource resource = new ClassPathResource("config/perplexity.xml");
			InputStream inputStream = resource.getInputStream();
			prop.loadFromXML(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String perplexityService(String content) throws Exception {

		String model = prop.getProperty("modelLlamaSmall");
		String prompt = prop.getProperty("prompt");
		
		/*
			{
			  "model": "llama-3.1-sonar-small-128k-online",
			  "messages": [
			    {"role": "system", "content": "간결하게"},
			    {"role": "user", "content": "안아줘요"}
			  ]
			}
			
			최종적으로 위 JSON 형태가 나온다.
			Message객체는 messages에 들어갈 내용
			AiRequest객체는 전체 JSON형태를 구성한다.
		*/
		
		//messages에 들어갈 내용 넣기
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("system", prompt));
		messages.add(new Message("user", content));
		
		//model, messages -> JSON -> GSON -> 문자열
		AiRequest AiRequest = new AiRequest(model, messages);
		String requestJson = new Gson().toJson(AiRequest);
		
		//api key들고 http형식으로 url찾아가서 requestJson던지기
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
		    .uri(URI.create(prop.getProperty("apiUrl")))
		    .header("Authorization", "Bearer " + prop.getProperty("apiKey"))
		    .header("Content-Type", "application/json")
		    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
		    .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		String result =  response.body(); //ai 문의 결과
		
		if(result == null || result.isEmpty()) {
			return "검색 결과가 없습니다.";
		}
		
		System.out.println(result);
		
		//답변받은 json에서 답변 추출
		
		//답변을 json으로 변환
		JsonObject root = new Gson().fromJson(result, JsonObject.class);
		
		// choices 배열 꺼내기
		JsonArray choices = root.getAsJsonArray("choices");

		// message.content 추출
		JsonObject firstChoice = choices.get(0).getAsJsonObject();
		JsonObject message = firstChoice.getAsJsonObject("message");
		String AiResponse = message.get("content").getAsString();
		
		//유니코드 디코딩
		AiResponse = StringEscapeUtils.unescapeJava(AiResponse);
		
		return AiResponse;
	}
	
	
/*
 * ------------perplexity 응답------------------
{
  "id": "01326666-293d-4e55-aa07-dce99ab900ee",
  "model": "llama-3.1-sonar-small-128k-online",
  "created": 1747835501,
  "usage": {
    "prompt_tokens": 21,
    "completion_tokens": 64,
    "total_tokens": 85,
    "search_context_size": "low"
  },
  "citations": [
    "https://vclock.kr/time/",
    "https://vclock.kr/time/%EC%84%9C%EC%9A%B8/",
    "https://onlinealarmkur.com/clock/ko/",
    "https://blog.naver.com/astraljoker/223606151266",
    "https://ko.thetimenow.com/worldclock.php"
  ],
  "object": "chat.completion",
  "choices": [
    {
      "index": 0,
      "finish_reason": "stop",
      "message": {
        "role": "assistant",
        "content": "\uc548\ub155\ud558\uc138\uc694 \ud604\uc7ac \uc2dc\uac04\uc744 \uc54c\ub824\ub4dc\ub9ac\uaca0\uc2b5\ub2c8\ub2e4. \ud604\uc7ac UTC \uc2dc\uac04\uc740 2025\ub144 5\uc6d4 21\uc77c 13:51\uc785\ub2c8\ub2e4. \ud55c\uad6d \uc2dc\uac04\uc740 UTC+9\uc774\ubbc0\ub85c, \ud604\uc7ac \ud55c\uad6d \uc2dc\uac04\uc740 2025\ub144 5\uc6d4 21\uc77c 22:51\uc785\ub2c8\ub2e4[1][2]."
      },
      "delta": {
        "role": "assistant",
        "content": ""
      }
    }
  ]
}
-------------------------------------------
===============유니코드 디코딩================
message.content
{
  "role": "assistant",
  "content": "안녕하세요 현재 시간을 알려드리겠습니다. 현재 UTC 시간은 2025년 5월 21일 13:51입니다. 한국 시간은 UTC+9이므로, 현재 한국 시간은 2025년 5월 21일 22:51입니다[1][2]."
}
*/

}
