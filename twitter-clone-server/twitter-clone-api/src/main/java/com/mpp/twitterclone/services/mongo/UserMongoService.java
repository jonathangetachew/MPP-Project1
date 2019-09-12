package com.mpp.twitterclone.services.mongo;

import com.mpp.twitterclone.model.Follow;
import com.mpp.twitterclone.model.User;
import com.mpp.twitterclone.repositories.FollowRepository;
import com.mpp.twitterclone.repositories.UserRepository;
import com.mpp.twitterclone.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jonathan on 9/8/2019.
 */

@Service
public class UserMongoService implements UserService {

	private final UserRepository userRepository;

	private final FollowRepository followRepository;

	public UserMongoService(UserRepository userRepository, FollowRepository followRepository) {
		this.userRepository = userRepository;
		this.followRepository = followRepository;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public List<User> findAllFollowers(String followedUserId) {
		List<Follow> follows = followRepository.findAllByFollowedUserId(followedUserId);

		return follows.stream()
				.map(follow -> findById(follow.getFollowerUserId())).collect(Collectors.toList());
	}

	@Override
	public List<User> findAllFollowing(String followerUserId) {
		List<Follow> follows = followRepository.findAllByFollowerUserId(followerUserId);

		return follows.stream()
				.map(follow -> findById(follow.getFollowerUserId())).collect(Collectors.toList());
	}

	@Override
	public User findById(String id) {
		// Todo: implement exception handling
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findUserByUsername(String username) {
		// Todo: implement exception handling
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public User create(User user) {
		// Todo: fix - lookup other project
		return userRepository.insert(user);
	}

	@Override
	public User followUser(String followedUserId, String followerUserId) {
		// Original Tweet
		User followedUser = findById(followedUserId);

		// Check for retweet record
		Optional<Follow> follow = followRepository.findByFollowerUserIdAndFollowedUserId(followerUserId, followedUserId);

		Integer currentFollowerCount = followedUser.getFollowersCount();

		if (follow.isPresent()) { // User performing the unfollow action

			// Remove record of current user favorite the tweet
			followRepository.deleteByFollowerUserIdAndAndFollowedUserId(follow.get().getFollowerUserId(),
																		follow.get().getFollowedUserId());

			// Update tweet data
			followedUser.setFollowersCount(--currentFollowerCount);

		} else { // User performing the favorite action
			// Keep a record of current user favorite the tweet
			followRepository.insert(Follow.builder().followedUserId(followedUserId)
													.followerUserId(followerUserId).build());

			// Update tweet data
			followedUser.setFollowersCount(++currentFollowerCount);
		}

		return update(followedUser, followedUserId);
	}

	@Override
	public User update(User newUser, String id) {
		return userRepository.findById(id)
				.map(u -> {

					// TODO: check if user performing the update is the owner
					// TODO: check it the the new username/email isn't already taken
					u.setUsername(newUser.getUsername());
					u.setPassword(newUser.getPassword());
					u.setName(newUser.getName());
					u.setEmail(newUser.getEmail());
					u.setGender(newUser.getGender());
					u.setDateOfBirth(newUser.getDateOfBirth());
					u.setProfileBannerUrl(newUser.getProfileBannerUrl());
					u.setProfileImageUrl(newUser.getProfileImageUrl());
					u.setPhoneNumber(newUser.getPhoneNumber());
					u.setUrl(newUser.getUrl());
					u.setDescription(newUser.getDescription());
					u.setProtect(newUser.getProtect());
					u.setVerified(newUser.getVerified());
					u.setFollowersCount(newUser.getFollowersCount());
					u.setFriendsCount(newUser.getFriendsCount());
					u.setRoles(newUser.getRoles());

					return userRepository.save(u);

				})
				// Todo: implement exception handling
				.orElse(null);
	}

	@Override
	public void delete(User user) {
		// Check if the Tweet exists in db
		findById(user.getId());

		userRepository.delete(user);
	}

	@Override
	public void deleteById(String id) {
		// TODO: check if user performing action is the owner

		// Check if the Tweet exists in db
		findById(id);

		userRepository.deleteById(id);
	}
}
