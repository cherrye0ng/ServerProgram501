package com.busanit501.boot501.service;

import com.busanit501.boot501.domain.Board;
import com.busanit501.boot501.dto.BoardDTO;
import com.busanit501.boot501.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
// all or nothing,
@Transactional
public class BoardServiceImpl implements BoardService {
  // 의존성 주입
  private final BoardRepository boardRepository;
  private final ModelMapper modelMapper;

  @Override
  public Long register(BoardDTO boardDTO) {
    // 화면에서 작성한 게시글 내용이 DTO 있고, -> VO 대신 entity 클래스 이용중.
    Board board = modelMapper.map(boardDTO, Board.class);
    Long bno = boardRepository.save(board).getBno();
    return bno;
  }

  @Override
  public BoardDTO read(Long bno) {
    // 1차 영속성 컨텍스트에서 조회한 내용을 가져오기.
    Optional<Board> result = boardRepository.findById(bno);
    // 만약 있다면, 엔티티 타입으로 담기.(VO 같은 개념)
    Board board = result.orElseThrow();
    // 엔티티 -> DTO 변환.
    BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

    return boardDTO;
  }

  @Override
  public void update(BoardDTO boardDTO) {
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    Board board = result.orElseThrow();
    // 만약 변경한다면, 제목과 내용만 변경하기. ,엔티티 클래스에서 메서드로 만듦.
    board.changeTitleAndContent(boardDTO.getTitle(),boardDTO.getContent());
    // 적용하기.
    boardRepository.save(board);
  }

  @Override
  public void delete(Long bno) {
    boardRepository.deleteById(bno);
  }
}






