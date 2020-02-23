package org.schabi.newpipe.extractor.services.youtube.extractors;

import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.channel.ChannelInfoItemExtractor;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeChannelLinkHandlerFactory;
import org.schabi.newpipe.extractor.utils.Utils;

/*
 * Created by Christian Schabesberger on 12.02.17.
 *
 * Copyright (C) Christian Schabesberger 2017 <chris.schabesberger@mailbox.org>
 * YoutubeChannelInfoItemExtractor.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

public class YoutubeChannelInfoItemExtractor implements ChannelInfoItemExtractor {
    private JsonObject channelInfoItem;

    public YoutubeChannelInfoItemExtractor(JsonObject channelInfoItem) {
        this.channelInfoItem = channelInfoItem;
    }

    @Override
    public String getThumbnailUrl() throws ParsingException {
        try {
            return channelInfoItem.getObject("thumbnail").getArray("thumbnails").getObject(0).getString("url");
        } catch (Exception e) {
            throw new ParsingException("Could not get thumbnail url", e);
        }
    }

    @Override
    public String getName() throws ParsingException {
        try {
            return channelInfoItem.getObject("title").getString("simpleText");
        } catch (Exception e) {
            throw new ParsingException("Could not get name", e);
        }
    }

    @Override
    public String getUrl() throws ParsingException {
        try {
            String id = "channel/" + channelInfoItem.getString("channelId"); // Does prepending 'channel/' always work?
            return YoutubeChannelLinkHandlerFactory.getInstance().getUrl(id);
        } catch (Exception e) {
            throw new ParsingException("Could not get url", e);
        }
    }

    @Override
    public long getSubscriberCount() {
        // TODO: get subscriber count, it's in subscriberCountText.simpleText as a string like "103M subscribers"
        return -1;
    }

    @Override
    public long getStreamCount() throws ParsingException {
        try {
            return Long.parseLong(Utils.removeNonDigitCharacters(channelInfoItem.getObject("videoCountText")
                    .getArray("runs").getObject(0).getString("text")));
        } catch (Exception e) {
            throw new ParsingException("Could not get stream count", e);
        }
    }

    @Override
    public String getDescription() throws ParsingException {
        try {
            return channelInfoItem.getObject("descriptionSnippet").getArray("runs").getObject(0).getString("text");
        } catch (Exception e) {
            throw new ParsingException("Could not get description", e);
        }
    }
}
