package com.hugme.plz.ai.model.vo;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AiRequest {
	private String model;
	private List<Message> messages;
}
