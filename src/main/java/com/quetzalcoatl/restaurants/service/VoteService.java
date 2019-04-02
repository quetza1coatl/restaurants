package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Vote;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import com.quetzalcoatl.restaurants.repository.CrudUserRepository;
import com.quetzalcoatl.restaurants.repository.CrudVoteRepository;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("voteService")
public class VoteService {

    private final CrudVoteRepository repository;
    private final CrudUserRepository userRepository;
    private final CrudRestaurantRepository restaurantRepository;

    private static final LocalTime REVOTE_TIME = LocalTime.of(11,0);

    @Autowired
    public VoteService(CrudVoteRepository repository,
                       CrudUserRepository userRepository,
                       CrudRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Vote create(int restaurantId, int userId, LocalDateTime dateTime) {
        Vote vote = new Vote();
        vote.setRestaurant(checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId));
        vote.setUser(checkNotFoundWithId(userRepository.findById(userId).orElse(null), userId));
        vote.setDateTime(dateTime);
        return repository.save(vote);
    }

    @Transactional
    public Vote update(int voteId, int newRestaurantId, LocalDateTime dateTime) {
        Vote vote = checkNotFoundWithId(repository.findById(voteId).orElse(null), voteId);
        if (isLate(dateTime, vote.getDate())) {
            throw new LateToVoteException("It is late to change your vote");
        }
        vote.setRestaurant(restaurantRepository.getOne(newRestaurantId));
        vote.setDateTime(dateTime);
        return repository.save(vote);

    }

    public List<Vote> getAll() {
        return repository.findAll();
    }

    public Integer getVoteIdByUserIdAndDate(int userId, LocalDate date){
        return repository.getVoteIdByUserIdAndDate(userId, date);
    }

    //vote history
    public List<Vote> getAllByRestaurantId(int id) {
        return repository.getByRestaurantId(id);
    }

    public Vote get(Integer id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }


    private boolean isLate(LocalDateTime actualDateTime, LocalDate dateFromVote) {
        return actualDateTime.toLocalTime().isAfter(REVOTE_TIME) || !actualDateTime.toLocalDate().isEqual(dateFromVote);

    }

}
