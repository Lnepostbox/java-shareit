package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;

@Mapper
public class ItemMapper {

    public static Item toItem(ItemDtoRequest itemDtoRequest) {
        Item item = new Item();
        item.setId(itemDtoRequest.getId());
        item.setName(itemDtoRequest.getName());
        item.setDescription(itemDtoRequest.getDescription());
        item.setAvailable(itemDtoRequest.getAvailable());
        return item;
    }

    public static ItemDtoResponse toItemDtoResponse(Item item) {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        itemDtoResponse.setId(item.getId());
        itemDtoResponse.setName(item.getName());
        itemDtoResponse.setDescription(item.getDescription());
        itemDtoResponse.setAvailable(item.getAvailable());
        itemDtoResponse.setOwner(new ItemDtoResponse.ItemOwner(item.getOwner().getId(), item.getOwner().getName()));
        itemDtoResponse.setLastBooking(null);
        itemDtoResponse.setNextBooking(null);
        itemDtoResponse.setComments(new ArrayList<>());
        itemDtoResponse.setRequestId(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDtoResponse;
    }
}