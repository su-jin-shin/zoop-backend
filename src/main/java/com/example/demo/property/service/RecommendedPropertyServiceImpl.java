package com.example.demo.property.service;

import com.example.demo.chat.domain.Message;
import com.example.demo.chat.repository.MessageRepository;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.RecommendedProperty;
import com.example.demo.property.dto.RecommendedPropertyDto;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.property.repository.RecommendedPropertyRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class RecommendedPropertyServiceImpl implements RecommendedPropertyService {

    private final PropertyRepository propertyRepository;
    private final RecommendedPropertyRepository recommendedPropertyRepository;
    private final MessageRepository messageRepository;


    @Override
    @Transactional
    public void saveRecommendedPropertyForMessage(Long messageId, List<RecommendedPropertyDto> recommendedProperties) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 message가 존재하지 않습니다: " + messageId));

        for (RecommendedPropertyDto p : recommendedProperties) {
            Property property = propertyRepository.findById(p.getPropertyId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 property가 존재하지 않습니다: " + p.getPropertyId()));

            RecommendedProperty recommendedProperty = RecommendedProperty.builder()
                    .message(message)
                    .property(property)
                    .build();

            recommendedPropertyRepository.save(recommendedProperty);
        }
    }

}
