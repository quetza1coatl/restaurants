package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import com.quetzalcoatl.restaurants.repository.CrudUserRepository;
import com.quetzalcoatl.restaurants.repository.CrudVotesRepository;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("votesService")
public class VotesService {

    private final CrudVotesRepository repository;
    private final CrudUserRepository userRepository;
    private final CrudRestaurantRepository restaurantRepository;

    @Autowired
    public VotesService(CrudVotesRepository repository,
                        CrudUserRepository userRepository,
                        CrudRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Votes create(int restaurantId, int userId) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (isLateByTime(dateTime.toLocalTime())) {
            throw new LateToVoteException("It is late to vote");
        }
        Votes vote = new Votes();
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        vote.setDateTime(dateTime);
        return repository.save(vote);


    }

    @Transactional
    public Votes update(Votes vote, int newRestaurantId) {
        Assert.notNull(vote, "vote must not be null");
        LocalDateTime dateTime = LocalDateTime.now();
        if (isLate(dateTime.toLocalTime(), dateTime.toLocalDate(), vote.getDateTime().toLocalDate())) {
            throw new LateToVoteException("It is late to vote");
        }
        vote.setRestaurant(restaurantRepository.getOne(newRestaurantId));
        vote.setDateTime(dateTime);
        return checkNotFoundWithId(repository.save(vote), vote.getId());

    }

    public List<Votes> getAll() {
        return repository.findAll();
    }

    public List<Votes> getAllByRestaurantId(int id) {
        return repository.getByRestaurantId(id);
    }

    public Votes findById(int id){
        return repository.findById(id);
    }

    private boolean isLate(LocalTime time, LocalDate actualDate, LocalDate dateFromVote) {
        return time.isAfter(LocalTime.of(11, 0)) || !actualDate.isEqual(dateFromVote);

    }

    private boolean isLateByTime(LocalTime time) {
        return time.isAfter(LocalTime.of(11, 0));
    }
}
