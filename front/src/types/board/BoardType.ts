export interface BoardType {
  content: string,
  email: string,
  id: number,
  name: string,
  reportingDate: string,
  title: string,
  commentList: [],
  fileList: []
}

export interface BoardListType {
  boardList : [],
  pageNum: number,
  totalPages: number
}

export interface CommentType {
  boardId: number,
  content: string,
  email: string,
  id: number,
  name: string,
  reportingDate: string,
}

export interface BoardSearchType {
  searchType : string,
  searchWord : string,
  pageNum : number
}

export interface FileType {
  id : number,
  originFileName : string,
  fileSize : number
}