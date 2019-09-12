package com.mpp.twitterclone.controllers.v1;

import com.mpp.twitterclone.controllers.v1.resourceassemblers.TweetResourceAssembler;
import com.mpp.twitterclone.model.Tweet;
import com.mpp.twitterclone.services.TweetService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jonathan on 9/8/2019.
 */

@RestController
@RequestMapping(value = TweetController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE,
				consumes = MediaType.APPLICATION_JSON_VALUE)
public class TweetController {

	public static final String BASE_URL = "/api/v1/tweets";

	private final TweetService tweetService;

	private final TweetResourceAssembler tweetResourceAssembler;

	public TweetController(TweetService tweetService, TweetResourceAssembler tweetResourceAssembler) {
		this.tweetService = tweetService;
		this.tweetResourceAssembler = tweetResourceAssembler;
	}

	///> Get Mappings
	@GetMapping
	public Resources<Resource<Tweet>> getAllTweets() {
		List<Resource<Tweet>> tweets = tweetService.findAll().stream()
				.map(tweetResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(tweets,
				linkTo(methodOn(TweetController.class).getAllTweets()).withSelfRel());
	}

	@GetMapping("/user/{username}")
	public Resources<Resource<Tweet>> getAllTweetsByUsername(@PathVariable String username) {
		List<Resource<Tweet>> tweets = tweetService.findAllTweetsByUsername(username).stream()
				.map(tweetResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(tweets,
				linkTo(methodOn(TweetController.class).getAllTweetsByUsername(username)).withSelfRel());
	}

	@GetMapping("/{id}")
	public Resource<Tweet> getTweetById(@PathVariable String id) {
		return tweetResourceAssembler.toResource(tweetService.findById(id));
	}

	@GetMapping("/{id}/replies")
	public Resources<Resource<Tweet>> getTweetReplies(@PathVariable String id) {
		List<Resource<Tweet>> tweets = tweetService.findAllReplies(id).stream()
				.map(tweetResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(tweets,
				linkTo(methodOn(TweetController.class).getTweetReplies(id)).withSelfRel());
	}

	// todo: getFavoriteTweets

	///> Post Mappings
	@PostMapping("/create")
	public ResponseEntity<Resource<Tweet>> createTweet(@RequestBody Tweet tweet) throws URISyntaxException {
		Resource<Tweet> tweetResource = tweetResourceAssembler.toResource(tweetService.create(tweet));

		return ResponseEntity
				.created(new URI(tweetResource.getId().expand().getHref()))
				.body(tweetResource);
	}

	@PostMapping("/{id}/reply")
	public ResponseEntity<Resource<Tweet>> reply(@RequestBody Tweet tweet,
	                                             @PathVariable String id) throws URISyntaxException {
		Resource<Tweet> tweetResource = tweetResourceAssembler.toResource(tweetService.replyToTweet(tweet, id));

		return ResponseEntity
				.created(new URI(tweetResource.getId().expand().getHref()))
				.body(tweetResource);
	}

//	@PostMapping("/{id}/retweet")
//	public ResponseEntity<Resource<Tweet>> retweet(@PathVariable String id) throws URISyntaxException {
//		Resource<Tweet> tweetResource = tweetResourceAssembler.toResource(tweetService.retweetTweet(id, "test"));
//
//		return ResponseEntity
//				.created(new URI(tweetResource.getId().expand().getHref()))
//				.body(tweetResource);
//	}

	@PostMapping("/{id}/favorite")
	public ResponseEntity<Resource<Tweet>> favorite(@PathVariable String id) throws URISyntaxException {
		// Todo: change user id to id from principal object
		Resource<Tweet> tweetResource = tweetResourceAssembler.toResource(tweetService.favoriteTweet(id, "test"));

		return ResponseEntity
				.created(new URI(tweetResource.getId().expand().getHref()))
				.body(tweetResource);
	}

	///> Put Mappings
	@PutMapping("/{id}/update")
	public ResponseEntity<Resource<Tweet>> updateTweet(@RequestBody Tweet tweet,
	                                                   @PathVariable String id) throws URISyntaxException {
		Resource<Tweet> tweetResource = tweetResourceAssembler.toResource(tweetService.update(tweet, id));

		return ResponseEntity
				.created(new URI(tweetResource.getId().expand().getHref()))
				.body(tweetResource);
	}

	///> Delete Mappings
	@DeleteMapping("/{id}/remove")
	public ResponseEntity<?> deleteTweet(@PathVariable String id) {
		tweetService.deleteById(id);

		Map<String, String> responseMessage = new HashMap<>();
		responseMessage.put("message", "Tweet Removed Successfully");

		return ResponseEntity
				.accepted()
				.contentType(MediaType.APPLICATION_JSON)
				.body(responseMessage);
	}
}
